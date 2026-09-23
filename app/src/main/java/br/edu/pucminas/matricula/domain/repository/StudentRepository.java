package br.edu.pucminas.matricula.domain.repository;

import br.edu.pucminas.matricula.domain.model.Student;
import java.util.List;
import java.util.Optional;

public interface StudentRepository {
  Optional<Student> findById(Long id);

  Optional<Student> findByUserId(Long userId);

  List<Student> findAll();

  Student save(Student student);

  void deleteById(Long id);
}
