package br.edu.pucminas.matricula.domain.repository;

import br.edu.pucminas.matricula.domain.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
  Optional<User> findById(Long id);

  Optional<User> findByUsername(String username);

  List<User> findAll();

  boolean existsAny();

  User save(User user);

  void deleteById(Long id);
}
