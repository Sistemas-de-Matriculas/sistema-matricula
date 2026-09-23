package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.domain.model.Offering;
import br.edu.pucminas.matricula.domain.repository.CourseRepository;
import br.edu.pucminas.matricula.domain.repository.DisciplineRepository;
import br.edu.pucminas.matricula.domain.repository.OfferingRepository;
import br.edu.pucminas.matricula.domain.repository.ProfessorRepository;
import br.edu.pucminas.matricula.domain.repository.SemesterRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListOfferingsUseCase {
  private final OfferingRepository offeringRepository;
  private final SemesterRepository semesterRepository;
  private final DisciplineRepository disciplineRepository;
  private final CourseRepository courseRepository;
  private final ProfessorRepository professorRepository;

  public ListOfferingsUseCase(
      OfferingRepository offeringRepository,
      SemesterRepository semesterRepository,
      DisciplineRepository disciplineRepository,
      CourseRepository courseRepository,
      ProfessorRepository professorRepository) {
    this.offeringRepository = offeringRepository;
    this.semesterRepository = semesterRepository;
    this.disciplineRepository = disciplineRepository;
    this.courseRepository = courseRepository;
    this.professorRepository = professorRepository;
  }

  @Transactional(readOnly = true)
  public List<OfferingResponse> execute(Long semesterId) {
    var semester = semesterRepository.findById(semesterId).orElse(null);
    String semesterCode = semester != null ? semester.code() : "?";

    return offeringRepository.findBySemesterId(semesterId).stream()
        .map(o -> toResponse(o, semesterCode))
        .toList();
  }

  private OfferingResponse toResponse(Offering offering, String semesterCode) {
    var discipline = disciplineRepository.findById(offering.disciplineId()).orElse(null);
    var course = discipline != null ? courseRepository.findById(discipline.courseId()).orElse(null) : null;
    var professor = discipline != null ? professorRepository.findById(discipline.professorId()).orElse(null) : null;

    return new OfferingResponse(
        offering.id(),
        offering.semesterId(),
        semesterCode,
        offering.disciplineId(),
        discipline != null ? discipline.name() : "?",
        course != null ? course.name() : "?",
        professor != null ? professor.name() : "?",
        offering.capacity(),
        offering.status());
  }
}

