package br.edu.pucminas.matricula.infrastructure.persistence;

import br.edu.pucminas.matricula.domain.model.Course;
import br.edu.pucminas.matricula.domain.repository.CourseRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class SpringCourseRepository implements CourseRepository {
  private final CourseJpaRepository courseJpaRepository;

  public SpringCourseRepository(CourseJpaRepository courseJpaRepository) {
    this.courseJpaRepository = courseJpaRepository;
  }

  @Override
  public Optional<Course> findById(Long id) {
    return courseJpaRepository.findById(id).map(this::toDomain);
  }

  @Override
  public Optional<Course> findByName(String name) {
    return courseJpaRepository.findByName(name).map(this::toDomain);
  }

  @Override
  public List<Course> findAll() {
    return courseJpaRepository.findAll().stream().map(this::toDomain).toList();
  }

  @Override
  public Course save(Course course) {
    CourseEntity saved = courseJpaRepository.save(new CourseEntity(course.name(), course.credits()));
    return toDomain(saved);
  }

  @Override
  public void deleteById(Long id) {
    courseJpaRepository.deleteById(id);
  }

  private Course toDomain(CourseEntity entity) {
    return new Course(entity.getId(), entity.getName(), entity.getCredits());
  }
}
