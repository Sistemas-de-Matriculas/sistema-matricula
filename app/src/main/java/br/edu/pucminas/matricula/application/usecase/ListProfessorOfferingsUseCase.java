package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.domain.exception.ValidationException;
import br.edu.pucminas.matricula.domain.model.Discipline;
import br.edu.pucminas.matricula.domain.model.Offering;
import br.edu.pucminas.matricula.domain.model.Professor;
import br.edu.pucminas.matricula.domain.repository.CourseRepository;
import br.edu.pucminas.matricula.domain.repository.DisciplineRepository;
import br.edu.pucminas.matricula.domain.repository.EnrollmentRepository;
import br.edu.pucminas.matricula.domain.repository.OfferingRepository;
import br.edu.pucminas.matricula.domain.repository.ProfessorRepository;
import br.edu.pucminas.matricula.domain.repository.SemesterRepository;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** UC10 — lista as disciplinas ofertadas que o professor autenticado leciona (RN09). */
@Service
public class ListProfessorOfferingsUseCase {
  private final ProfessorRepository professorRepository;
  private final DisciplineRepository disciplineRepository;
  private final OfferingRepository offeringRepository;
  private final SemesterRepository semesterRepository;
  private final CourseRepository courseRepository;
  private final EnrollmentRepository enrollmentRepository;

  public ListProfessorOfferingsUseCase(
      ProfessorRepository professorRepository,
      DisciplineRepository disciplineRepository,
      OfferingRepository offeringRepository,
      SemesterRepository semesterRepository,
      CourseRepository courseRepository,
      EnrollmentRepository enrollmentRepository) {
    this.professorRepository = professorRepository;
    this.disciplineRepository = disciplineRepository;
    this.offeringRepository = offeringRepository;
    this.semesterRepository = semesterRepository;
    this.courseRepository = courseRepository;
    this.enrollmentRepository = enrollmentRepository;
  }

  @Transactional(readOnly = true)
  public List<ProfessorOfferingResponse> execute(Long authenticatedUserId) {
    Professor professor = professorRepository.findByUserId(authenticatedUserId)
        .orElseThrow(() -> new ValidationException("Professor não encontrado para o usuário autenticado."));

    return disciplineRepository.findByProfessorId(professor.id()).stream()
        .flatMap(d -> offeringRepository.findByDisciplineId(d.id()).stream().map(o -> toResponse(o, d)))
        .sorted(Comparator.comparing(ProfessorOfferingResponse::semesterCode).reversed()
            .thenComparing(ProfessorOfferingResponse::disciplineName))
        .toList();
  }

  private ProfessorOfferingResponse toResponse(Offering offering, Discipline discipline) {
    var semester = semesterRepository.findById(offering.semesterId()).orElse(null);
    var course = courseRepository.findById(discipline.courseId()).orElse(null);

    return new ProfessorOfferingResponse(
        offering.id(),
        offering.semesterId(),
        semester != null ? semester.code() : "?",
        discipline.id(),
        discipline.name(),
        course != null ? course.name() : "?",
        discipline.category(),
        offering.capacity(),
        enrollmentRepository.countEnrolledByOfferingId(offering.id()),
        offering.status());
  }
}
