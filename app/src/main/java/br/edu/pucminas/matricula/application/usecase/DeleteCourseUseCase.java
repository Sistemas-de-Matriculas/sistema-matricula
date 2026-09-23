package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.domain.exception.ValidationException;
import br.edu.pucminas.matricula.domain.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteCourseUseCase {
  private final CourseRepository courseRepository;

  public DeleteCourseUseCase(CourseRepository courseRepository) {
    this.courseRepository = courseRepository;
  }

  @Transactional
  public void execute(Long courseId) {
    if (courseId == null) {
      throw new ValidationException("ID inválido.");
    }
    if (courseRepository.findById(courseId).isEmpty()) {
      throw new ValidationException("Curso não encontrado.");
    }
    courseRepository.deleteById(courseId);
  }
}
