package br.edu.pucminas.matricula.domain.repository;

import br.edu.pucminas.matricula.domain.model.Professor;
import java.util.List;
import java.util.Optional;

public interface ProfessorRepository {
  Optional<Professor> findById(Long id);

  Optional<Professor> findByUserId(Long userId);

  List<Professor> findAll();

  Professor save(Professor professor);

  void deleteById(Long id);
}
