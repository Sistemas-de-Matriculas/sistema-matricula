package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.domain.exception.ValidationException;
import br.edu.pucminas.matricula.domain.model.Enrollment;
import br.edu.pucminas.matricula.domain.model.EnrollmentStatus;
import br.edu.pucminas.matricula.domain.model.Offering;
import br.edu.pucminas.matricula.domain.model.Semester;
import br.edu.pucminas.matricula.domain.model.Student;
import br.edu.pucminas.matricula.domain.repository.EnrollmentRepository;
import br.edu.pucminas.matricula.domain.repository.OfferingRepository;
import br.edu.pucminas.matricula.domain.repository.SemesterRepository;
import br.edu.pucminas.matricula.domain.repository.StudentRepository;
import java.time.OffsetDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CancelStudentEnrollmentUseCase {
  private final EnrollmentRepository enrollmentRepository;
  private final StudentRepository studentRepository;
  private final OfferingRepository offeringRepository;
  private final SemesterRepository semesterRepository;

  public CancelStudentEnrollmentUseCase(
      EnrollmentRepository enrollmentRepository,
      StudentRepository studentRepository,
      OfferingRepository offeringRepository,
      SemesterRepository semesterRepository) {
    this.enrollmentRepository = enrollmentRepository;
    this.studentRepository = studentRepository;
    this.offeringRepository = offeringRepository;
    this.semesterRepository = semesterRepository;
  }

  @Transactional
  public void execute(Long enrollmentId, Long authenticatedUserId) {
    if (enrollmentId == null) {
      throw new ValidationException("Identificador da matrícula é obrigatório.");
    }

    Student student = studentRepository.findByUserId(authenticatedUserId)
        .orElseThrow(() -> new ValidationException("Aluno não encontrado para o usuário autenticado."));

    Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
        .orElseThrow(() -> new ValidationException("Matrícula não encontrada."));

    if (!enrollment.studentId().equals(student.id())) {
      throw new ValidationException("Matrícula não pertence ao aluno autenticado.");
    }

    if (enrollment.status() == EnrollmentStatus.CANCELLED) {
      throw new ValidationException("Matrícula já cancelada.");
    }

    Offering offering = offeringRepository.findById(enrollment.offeringId()).orElseThrow();
    Semester semester = semesterRepository.findById(offering.semesterId()).orElseThrow();
    ensureEnrollmentPeriodOpen(semester);

    enrollmentRepository.save(new Enrollment(
        enrollment.id(),
        enrollment.offeringId(),
        enrollment.studentId(),
        EnrollmentStatus.CANCELLED,
        enrollment.createdAt(),
        OffsetDateTime.now()));
  }

  private void ensureEnrollmentPeriodOpen(Semester semester) {
    if (!semester.enrollmentOpen()) {
      throw new ValidationException("Período de matrículas não está aberto (RN03).");
    }
    OffsetDateTime now = OffsetDateTime.now();
    if (semester.enrollmentStart() != null && now.isBefore(semester.enrollmentStart())) {
      throw new ValidationException("Período de matrículas ainda não foi iniciado (RN03).");
    }
    if (semester.enrollmentEnd() != null && !now.isBefore(semester.enrollmentEnd())) {
      throw new ValidationException("Período de matrículas encerrado (RN03).");
    }
  }
}
