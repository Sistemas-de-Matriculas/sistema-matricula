package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.domain.exception.ValidationException;
import br.edu.pucminas.matricula.domain.model.Discipline;
import br.edu.pucminas.matricula.domain.model.DisciplineCategory;
import br.edu.pucminas.matricula.domain.repository.CourseRepository;
import br.edu.pucminas.matricula.domain.repository.DisciplineRepository;
import br.edu.pucminas.matricula.domain.repository.ProfessorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateDisciplineUseCase {
  private final DisciplineRepository disciplineRepository;
  private final CourseRepository courseRepository;
  private final ProfessorRepository professorRepository;

  public CreateDisciplineUseCase(
      DisciplineRepository disciplineRepository,
      CourseRepository courseRepository,
      ProfessorRepository professorRepository) {
    this.disciplineRepository = disciplineRepository;
    this.courseRepository = courseRepository;
    this.professorRepository = professorRepository;
  }

  @Transactional
  public DisciplineResponse execute(CreateDisciplineRequest request) {
    if (request == null) {
      throw new ValidationException("Requisição inválida.");
    }

    String name = normalize(request.name());
    if (name.isEmpty()) {
      throw new ValidationException("Nome é obrigatório.");
    }
    if (name.length() > 120) {
      throw new ValidationException("Nome deve ter no máximo 120 caracteres.");
    }
    if (request.courseId() == null) {
      throw new ValidationException("Curso é obrigatório.");
    }
    if (request.professorId() == null) {
      throw new ValidationException("Professor é obrigatório.");
    }

    var course = courseRepository.findById(request.courseId())
        .orElseThrow(() -> new ValidationException("Curso não encontrado."));
    var professor = professorRepository.findById(request.professorId())
        .orElseThrow(() -> new ValidationException("Professor não encontrado."));

    DisciplineCategory category = request.category() != null ? request.category() : DisciplineCategory.MANDATORY;

    Discipline saved = disciplineRepository.save(
        new Discipline(null, name, request.courseId(), request.professorId(), category));
    return new DisciplineResponse(
        saved.id(),
        saved.name(),
        course.id(),
        course.name(),
        professor.id(),
        professor.name(),
        category);
  }

  private String normalize(String value) {
    if (value == null) {
      return "";
    }
    return value.trim();
  }
}
