package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.domain.model.Discipline;
import br.edu.pucminas.matricula.domain.repository.CourseRepository;
import br.edu.pucminas.matricula.domain.repository.DisciplineRepository;
import br.edu.pucminas.matricula.domain.repository.ProfessorRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListDisciplinesUseCase {
  private final DisciplineRepository disciplineRepository;
  private final CourseRepository courseRepository;
  private final ProfessorRepository professorRepository;

  public ListDisciplinesUseCase(
      DisciplineRepository disciplineRepository,
      CourseRepository courseRepository,
      ProfessorRepository professorRepository) {
    this.disciplineRepository = disciplineRepository;
    this.courseRepository = courseRepository;
    this.professorRepository = professorRepository;
  }

  @Transactional(readOnly = true)
  public List<DisciplineResponse> execute() {
    return disciplineRepository.findAll().stream().map(this::toResponse).toList();
  }

  private DisciplineResponse toResponse(Discipline discipline) {
    var course = courseRepository.findById(discipline.courseId()).orElse(null);
    var professor = professorRepository.findById(discipline.professorId()).orElse(null);
    return new DisciplineResponse(
        discipline.id(),
        discipline.name(),
        discipline.courseId(),
        course != null ? course.name() : "?",
        discipline.professorId(),
        professor != null ? professor.name() : "?",
        discipline.category());
  }
}
