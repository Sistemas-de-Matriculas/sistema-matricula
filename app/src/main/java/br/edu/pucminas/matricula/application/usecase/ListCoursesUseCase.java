package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.domain.repository.CourseRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListCoursesUseCase {
  private final CourseRepository courseRepository;

  public ListCoursesUseCase(CourseRepository courseRepository) {
    this.courseRepository = courseRepository;
  }

  @Transactional(readOnly = true)
  public List<CourseResponse> execute() {
    return courseRepository.findAll().stream()
        .map(c -> new CourseResponse(c.id(), c.name(), c.credits()))
        .toList();
  }
}
