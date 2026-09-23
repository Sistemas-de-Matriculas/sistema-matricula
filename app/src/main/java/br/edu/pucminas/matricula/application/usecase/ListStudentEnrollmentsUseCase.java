package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.domain.exception.ValidationException;
import br.edu.pucminas.matricula.domain.model.Discipline;
import br.edu.pucminas.matricula.domain.model.Enrollment;
import br.edu.pucminas.matricula.domain.model.Offering;
import br.edu.pucminas.matricula.domain.model.Semester;
import br.edu.pucminas.matricula.domain.model.Student;
import br.edu.pucminas.matricula.domain.repository.DisciplineRepository;
import br.edu.pucminas.matricula.domain.repository.EnrollmentRepository;
import br.edu.pucminas.matricula.domain.repository.OfferingRepository;
import br.edu.pucminas.matricula.domain.repository.SemesterRepository;
import br.edu.pucminas.matricula.domain.repository.StudentRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListStudentEnrollmentsUseCase {
  private final StudentRepository studentRepository;
  private final SemesterRepository semesterRepository;
  private final EnrollmentRepository enrollmentRepository;
  private final OfferingRepository offeringRepository;
  private final DisciplineRepository disciplineRepository;

  public ListStudentEnrollmentsUseCase(
      StudentRepository studentRepository,
      SemesterRepository semesterRepository,
      EnrollmentRepository enrollmentRepository,
      OfferingRepository offeringRepository,
      DisciplineRepository disciplineRepository) {
    this.studentRepository = studentRepository;
    this.semesterRepository = semesterRepository;
    this.enrollmentRepository = enrollmentRepository;
    this.offeringRepository = offeringRepository;
    this.disciplineRepository = disciplineRepository;
  }

  @Transactional(readOnly = true)
  public List<EnrollmentResponse> execute(Long semesterId, Long authenticatedUserId) {
    if (semesterId == null) {
      throw new ValidationException("Semestre é obrigatório.");
    }

    Student student = studentRepository.findByUserId(authenticatedUserId)
        .orElseThrow(() -> new ValidationException("Aluno não encontrado para o usuário autenticado."));

    Semester semester = semesterRepository.findById(semesterId)
        .orElseThrow(() -> new ValidationException("Semestre não encontrado."));

    return enrollmentRepository
        .findByStudentIdAndSemesterId(student.id(), semester.id())
        .stream()
        .map(e -> toResponse(e, semester))
        .toList();
  }

  private EnrollmentResponse toResponse(Enrollment enrollment, Semester semester) {
    Offering offering = offeringRepository.findById(enrollment.offeringId()).orElse(null);
    Discipline discipline = offering != null
        ? disciplineRepository.findById(offering.disciplineId()).orElse(null)
        : null;

    return new EnrollmentResponse(
        enrollment.id(),
        semester.id(),
        semester.code(),
        enrollment.offeringId(),
        discipline != null ? discipline.id() : null,
        discipline != null ? discipline.name() : "?",
        discipline != null ? discipline.category() : null,
        enrollment.status(),
        enrollment.createdAt());
  }
}
