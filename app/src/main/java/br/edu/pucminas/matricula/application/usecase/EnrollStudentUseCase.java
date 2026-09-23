package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.domain.exception.ValidationException;
import br.edu.pucminas.matricula.domain.model.Discipline;
import br.edu.pucminas.matricula.domain.model.DisciplineCategory;
import br.edu.pucminas.matricula.domain.model.Enrollment;
import br.edu.pucminas.matricula.domain.model.EnrollmentStatus;
import br.edu.pucminas.matricula.domain.model.Offering;
import br.edu.pucminas.matricula.domain.model.OfferingStatus;
import br.edu.pucminas.matricula.domain.model.Semester;
import br.edu.pucminas.matricula.domain.model.Student;
import br.edu.pucminas.matricula.domain.repository.DisciplineRepository;
import br.edu.pucminas.matricula.domain.repository.EnrollmentRepository;
import br.edu.pucminas.matricula.domain.repository.OfferingRepository;
import br.edu.pucminas.matricula.domain.repository.SemesterRepository;
import br.edu.pucminas.matricula.domain.repository.StudentRepository;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnrollStudentUseCase {
  private static final int MAX_MANDATORY = 4;
  private static final int MAX_OPTIONAL = 2;

  private final SemesterRepository semesterRepository;
  private final OfferingRepository offeringRepository;
  private final DisciplineRepository disciplineRepository;
  private final EnrollmentRepository enrollmentRepository;
  private final StudentRepository studentRepository;
  private final BillingNotificationService billingNotificationService;

  public EnrollStudentUseCase(
      SemesterRepository semesterRepository,
      OfferingRepository offeringRepository,
      DisciplineRepository disciplineRepository,
      EnrollmentRepository enrollmentRepository,
      StudentRepository studentRepository,
      BillingNotificationService billingNotificationService) {
    this.semesterRepository = semesterRepository;
    this.offeringRepository = offeringRepository;
    this.disciplineRepository = disciplineRepository;
    this.enrollmentRepository = enrollmentRepository;
    this.studentRepository = studentRepository;
    this.billingNotificationService = billingNotificationService;
  }

  @Transactional
  public List<EnrollmentResponse> execute(EnrollStudentRequest request, Long authenticatedUserId) {
    if (request == null) {
      throw new ValidationException("Requisição inválida.");
    }
    if (request.semesterId() == null) {
      throw new ValidationException("Semestre é obrigatório.");
    }
    if (request.offeringIds() == null || request.offeringIds().isEmpty()) {
      throw new ValidationException("Selecione ao menos uma disciplina para matrícula.");
    }

    Student student = studentRepository.findByUserId(authenticatedUserId)
        .orElseThrow(() -> new ValidationException("Aluno não encontrado para o usuário autenticado."));

    Semester semester = semesterRepository.findById(request.semesterId())
        .orElseThrow(() -> new ValidationException("Semestre não encontrado."));
    ensureEnrollmentPeriodOpen(semester);

    List<Enrollment> existingActive = enrollmentRepository
        .findByStudentIdAndSemesterId(student.id(), semester.id())
        .stream()
        .filter(e -> e.status() == EnrollmentStatus.ENROLLED)
        .toList();

    int existingMandatory = 0;
    int existingOptional = 0;
    for (Enrollment e : existingActive) {
      Offering o = offeringRepository.findById(e.offeringId()).orElse(null);
      if (o == null) {
        continue;
      }
      Discipline d = disciplineRepository.findById(o.disciplineId()).orElse(null);
      if (d == null) {
        continue;
      }
      if (d.category() == DisciplineCategory.MANDATORY) {
        existingMandatory++;
      } else {
        existingOptional++;
      }
    }

    int newMandatory = 0;
    int newOptional = 0;
    List<EnrollmentResponse> responses = new ArrayList<>();

    for (Long offeringId : request.offeringIds()) {
      Offering offering = offeringRepository.findById(offeringId)
          .orElseThrow(() -> new ValidationException("Oferta não encontrada: " + offeringId));

      if (!offering.semesterId().equals(semester.id())) {
        throw new ValidationException("Oferta não pertence ao semestre informado.");
      }
      if (offering.status() == OfferingStatus.CANCELLED) {
        throw new ValidationException("Disciplina cancelada não pode ser cursada neste semestre.");
      }

      Discipline discipline = disciplineRepository.findById(offering.disciplineId())
          .orElseThrow(() -> new ValidationException("Disciplina não encontrada."));

      if (enrollmentRepository.existsActiveEnrollment(offering.id(), student.id())) {
        throw new ValidationException("Você já está matriculado na disciplina " + discipline.name() + ".");
      }

      long enrolled = enrollmentRepository.countEnrolledByOfferingId(offering.id());
      if (enrolled >= offering.capacity()) {
        throw new ValidationException(
            "Não há vagas disponíveis na disciplina " + discipline.name() + " (RN05).");
      }

      boolean isMandatory = discipline.category() == DisciplineCategory.MANDATORY;
      if (isMandatory) {
        if (existingMandatory + newMandatory >= MAX_MANDATORY) {
          throw new ValidationException(
              "Limite de " + MAX_MANDATORY + " disciplinas obrigatórias atingido (RN02).");
        }
        newMandatory++;
      } else {
        if (existingOptional + newOptional >= MAX_OPTIONAL) {
          throw new ValidationException(
              "Limite de " + MAX_OPTIONAL + " disciplinas optativas atingido (RN02).");
        }
        newOptional++;
      }

      Enrollment enrollment = enrollmentRepository.save(new Enrollment(
          null,
          offering.id(),
          student.id(),
          EnrollmentStatus.ENROLLED,
          OffsetDateTime.now(),
          null));

      responses.add(new EnrollmentResponse(
          enrollment.id(),
          semester.id(),
          semester.code(),
          offering.id(),
          discipline.id(),
          discipline.name(),
          discipline.category(),
          enrollment.status(),
          enrollment.createdAt()));
    }

    billingNotificationService.notifyEnrollment(student, existingActive);

    return responses;
  }

  private void ensureEnrollmentPeriodOpen(Semester semester) {
    if (!semester.enrollmentOpen()) {
      throw new ValidationException("Período de matrículas não está aberto para este semestre (RN03).");
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
