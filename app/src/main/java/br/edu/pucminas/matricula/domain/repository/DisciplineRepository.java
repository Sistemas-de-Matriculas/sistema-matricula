package br.edu.pucminas.matricula.domain.repository;

import br.edu.pucminas.matricula.domain.model.Discipline;
import java.util.List;
import java.util.Optional;

public interface DisciplineRepository {
  Optional<Discipline> findById(Long id);

  List<Discipline> findAll();

  List<Discipline> findByProfessorId(Long professorId);

  Discipline save(Discipline discipline);

  void deleteById(Long id);
}
