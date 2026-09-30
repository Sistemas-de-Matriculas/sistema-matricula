package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.domain.exception.ValidationException;
import br.edu.pucminas.matricula.domain.model.Discipline;
import br.edu.pucminas.matricula.domain.model.Enrollment;
import br.edu.pucminas.matricula.domain.model.Offering;
import br.edu.pucminas.matricula.domain.model.Professor;
import br.edu.pucminas.matricula.domain.repository.DisciplineRepository;
import br.edu.pucminas.matricula.domain.repository.EnrollmentRepository;
import br.edu.pucminas.matricula.domain.repository.OfferingRepository;
import br.edu.pucminas.matricula.domain.repository.ProfessorRepository;
import br.edu.pucminas.matricula.domain.repository.StudentRepository;
import br.edu.pucminas.matricula.domain.repository.UserRepository;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** UC10 — Consultar Alunos Matriculados em uma disciplina do professor autenticado (RN09). */
@Service
public class ListOfferingStudentsUseCase {
  private final ProfessorRepository professorRepository;
  private final OfferingRepository offeringRepository;
  private final DisciplineRepository disciplineRepository;
  private final EnrollmentRepository enrollmentRepository;
  private final StudentRepository studentRepository;
  private final UserRepository userRepository;

  public ListOfferingStudentsUseCase(
      ProfessorRepository professorRepository,
      OfferingRepository offeringRepository,
      DisciplineRepository disciplineRepository,
      EnrollmentRepository enrollmentRepository,
      StudentRepository studentRepository,
      UserRepository userRepository) {
    this.professorRepository = professorRepository;
    this.offeringRepository = offeringRepository;
    this.disciplineRepository = disciplineRepository;
    this.enrollmentRepository = enrollmentRepository;
    this.studentRepository = studentRepository;
    this.userRepository = userRepository;
  }

  @Transactional(readOnly = true)
  public List<EnrolledStudentResponse> execute(Long offeringId, Long authenticatedUserId) {
    if (offeringId == null) {
      throw new ValidationException("Oferta é obrigatória.");
    }

    Professor professor = professorRepository.findByUserId(authenticatedUserId)
        .orElseThrow(() -> new ValidationException("Professor não encontrado para o usuário autenticado."));

    Offering offering = offeringRepository.findById(offeringId)
        .orElseThrow(() -> new ValidationException("Oferta não encontrada."));
    Discipline discipline = disciplineRepository.findById(offering.disciplineId())
        .orElseThrow(() -> new ValidationException("Disciplina não encontrada."));

    if (!discipline.professorId().equals(professor.id())) {
      throw new ValidationException("Você só pode consultar alunos das disciplinas que leciona (RN09).");
    }

    return enrollmentRepository.findActiveByOfferingId(offering.id()).stream()
        .map(this::toResponse)
        .sorted(Comparator.comparing(EnrolledStudentResponse::studentName, String.CASE_INSENSITIVE_ORDER))
        .toList();
  }

  private EnrolledStudentResponse toResponse(Enrollment enrollment) {
    var student = studentRepository.findById(enrollment.studentId()).orElse(null);
    var user = student != null ? userRepository.findById(student.userId()).orElse(null) : null;

    return new EnrolledStudentResponse(
        enrollment.id(),
        enrollment.studentId(),
        student != null ? student.name() : "?",
        user != null ? user.username() : "?",
        enrollment.createdAt());
  }
}
