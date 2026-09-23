package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.domain.exception.ValidationException;
import br.edu.pucminas.matricula.domain.model.Offering;
import br.edu.pucminas.matricula.domain.model.OfferingStatus;
import br.edu.pucminas.matricula.domain.model.Semester;
import br.edu.pucminas.matricula.domain.repository.CourseRepository;
import br.edu.pucminas.matricula.domain.repository.DisciplineRepository;
import br.edu.pucminas.matricula.domain.repository.EnrollmentRepository;
import br.edu.pucminas.matricula.domain.repository.OfferingRepository;
import br.edu.pucminas.matricula.domain.repository.ProfessorRepository;
import br.edu.pucminas.matricula.domain.repository.SemesterRepository;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListOfferingAvailabilityUseCase {
  private final SemesterRepository semesterRepository;
  private final OfferingRepository offeringRepository;
  private final EnrollmentRepository enrollmentRepository;
  private final DisciplineRepository disciplineRepository;
  private final CourseRepository courseRepository;
  private final ProfessorRepository professorRepository;

  public ListOfferingAvailabilityUseCase(
      SemesterRepository semesterRepository,
      OfferingRepository offeringRepository,
      EnrollmentRepository enrollmentRepository,
      DisciplineRepository disciplineRepository,
      CourseRepository courseRepository,
      ProfessorRepository professorRepository) {
    this.semesterRepository = semesterRepository;
    this.offeringRepository = offeringRepository;
    this.enrollmentRepository = enrollmentRepository;
    this.disciplineRepository = disciplineRepository;
    this.courseRepository = courseRepository;
    this.professorRepository = professorRepository;
  }

  @Transactional(readOnly = true)
  public List<OfferingAvailabilityResponse> execute(Long semesterId) {
    if (semesterId == null) {
      throw new ValidationException("Semestre é obrigatório.");
    }

    Semester semester =
        semesterRepository.findById(semesterId).orElseThrow(() -> new ValidationException("Semestre não encontrado."));

    if (!isEnrollmentPeriodOpen(semester, OffsetDateTime.now())) {
      throw new ValidationException("Período de matrículas não está aberto para este semestre.");
    }

    return offeringRepository.findBySemesterId(semesterId).stream()
        .filter(o -> o.status() != OfferingStatus.CANCELLED)
        .map(o -> toResponse(o, semester.code()))
        .toList();
  }

  private OfferingAvailabilityResponse toResponse(Offering offering, String semesterCode) {
    var discipline = disciplineRepository.findById(offering.disciplineId()).orElse(null);
    var course = discipline != null ? courseRepository.findById(discipline.courseId()).orElse(null) : null;
    var professor = discipline != null ? professorRepository.findById(discipline.professorId()).orElse(null) : null;

    long enrolled = enrollmentRepository.countEnrolledByOfferingId(offering.id());
    long available = Math.max(0, (long) offering.capacity() - enrolled);

    return new OfferingAvailabilityResponse(
        offering.id(),
        offering.semesterId(),
        semesterCode,
        offering.disciplineId(),
        discipline != null ? discipline.name() : "?",
        course != null ? course.name() : "?",
        professor != null ? professor.name() : "?",
        offering.capacity(),
        enrolled,
        available,
        offering.status(),
        discipline != null ? discipline.category() : null);
  }

  private boolean isEnrollmentPeriodOpen(Semester semester, OffsetDateTime now) {
    if (!semester.enrollmentOpen()) {
      return false;
    }
    if (semester.enrollmentStart() != null && now.isBefore(semester.enrollmentStart())) {
      return false;
    }
    if (semester.enrollmentEnd() != null && !now.isBefore(semester.enrollmentEnd())) {
      return false;
    }
    return true;
  }
}
