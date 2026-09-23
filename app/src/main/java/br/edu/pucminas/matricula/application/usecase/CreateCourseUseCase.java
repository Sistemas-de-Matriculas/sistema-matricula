package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.domain.exception.ValidationException;
import br.edu.pucminas.matricula.domain.model.Course;
import br.edu.pucminas.matricula.domain.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateCourseUseCase {
  private final CourseRepository courseRepository;

  public CreateCourseUseCase(CourseRepository courseRepository) {
    this.courseRepository = courseRepository;
  }

  @Transactional
  public CourseResponse execute(CreateCourseRequest request) {
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

    Integer credits = request.credits();
    if (credits == null || credits <= 0) {
      throw new ValidationException("Créditos deve ser maior que zero.");
    }

    courseRepository.findByName(name).ifPresent(c -> {
      throw new ValidationException("Já existe um curso com esse nome.");
    });

    Course saved = courseRepository.save(new Course(null, name, credits));
    return new CourseResponse(saved.id(), saved.name(), saved.credits());
  }

  private String normalize(String value) {
    if (value == null) {
      return "";
    }
    return value.trim();
  }
}
