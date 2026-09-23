package br.edu.pucminas.matricula.domain.repository;

import br.edu.pucminas.matricula.domain.model.Semester;
import java.util.List;
import java.util.Optional;

public interface SemesterRepository {
  Optional<Semester> findById(Long id);

  Optional<Semester> findByCode(String code);

  List<Semester> findAll();

  Semester save(Semester semester);

  Semester update(Semester semester);
}
