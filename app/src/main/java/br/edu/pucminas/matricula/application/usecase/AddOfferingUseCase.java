package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.domain.exception.ValidationException;
import br.edu.pucminas.matricula.domain.model.Offering;
import br.edu.pucminas.matricula.domain.model.OfferingStatus;
import br.edu.pucminas.matricula.domain.repository.CourseRepository;
import br.edu.pucminas.matricula.domain.repository.DisciplineRepository;
import br.edu.pucminas.matricula.domain.repository.OfferingRepository;
import br.edu.pucminas.matricula.domain.repository.ProfessorRepository;
import br.edu.pucminas.matricula.domain.repository.SemesterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddOfferingUseCase {
  private final SemesterRepository semesterRepository;
  private final DisciplineRepository disciplineRepository;
  private final CourseRepository courseRepository;
  private final ProfessorRepository professorRepository;
  private final OfferingRepository offeringRepository;

  public AddOfferingUseCase(
      SemesterRepository semesterRepository,
      DisciplineRepository disciplineRepository,
      CourseRepository courseRepository,
      ProfessorRepository professorRepository,
      OfferingRepository offeringRepository) {
    this.semesterRepository = semesterRepository;
    this.disciplineRepository = disciplineRepository;
    this.courseRepository = courseRepository;
    this.professorRepository = professorRepository;
    this.offeringRepository = offeringRepository;
  }

  @Transactional
  public OfferingResponse execute(AddOfferingRequest request) {
    if (request == null) {
      throw new ValidationException("Requisição inválida.");
    }
    if (request.semesterId() == null) {
      throw new ValidationException("Semestre é obrigatório.");
    }
    if (request.disciplineId() == null) {
      throw new ValidationException("Disciplina é obrigatória.");
    }

    var semester = semesterRepository.findById(request.semesterId())
        .orElseThrow(() -> new ValidationException("Semestre não encontrado."));
    if (!semester.enrollmentOpen()) {
      throw new ValidationException("Período de matrículas não está aberto para este semestre.");
    }

    var discipline = disciplineRepository.findById(request.disciplineId())
        .orElseThrow(() -> new ValidationException("Disciplina não encontrada."));
    var course = courseRepository.findById(discipline.courseId()).orElse(null);
    var professor = professorRepository.findById(discipline.professorId()).orElse(null);

    Offering offering = offeringRepository.save(
        new Offering(null, semester.id(), discipline.id(), 60, OfferingStatus.OFFERED));

    return new OfferingResponse(
        offering.id(),
        semester.id(),
        semester.code(),
        discipline.id(),
        discipline.name(),
        course != null ? course.name() : "?",
        professor != null ? professor.name() : "?",
        offering.capacity(),
        offering.status());
  }
}

