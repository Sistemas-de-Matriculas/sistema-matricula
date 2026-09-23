package br.edu.pucminas.matricula.domain.repository;

import br.edu.pucminas.matricula.domain.model.Course;
import java.util.List;
import java.util.Optional;

public interface CourseRepository {
  Optional<Course> findById(Long id);

  Optional<Course> findByName(String name);

  List<Course> findAll();

  Course save(Course course);

  void deleteById(Long id);
}
