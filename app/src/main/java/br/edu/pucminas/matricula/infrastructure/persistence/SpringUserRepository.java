package br.edu.pucminas.matricula.infrastructure.persistence;

import br.edu.pucminas.matricula.domain.model.User;
import br.edu.pucminas.matricula.domain.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class SpringUserRepository implements UserRepository {
  private final UserJpaRepository userJpaRepository;

  public SpringUserRepository(UserJpaRepository userJpaRepository) {
    this.userJpaRepository = userJpaRepository;
  }

  @Override
  public Optional<User> findById(Long id) {
    return userJpaRepository.findById(id).map(this::toDomain);
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return userJpaRepository.findByUsername(username).map(this::toDomain);
  }

  @Override
  public List<User> findAll() {
    return userJpaRepository.findAll().stream().map(this::toDomain).toList();
  }

  @Override
  public boolean existsAny() {
    return userJpaRepository.count() > 0;
  }

  @Override
  public User save(User user) {
    UserEntity saved = userJpaRepository.save(new UserEntity(user.username(), user.passwordHash(), user.role()));
    return toDomain(saved);
  }

  @Override
  public void deleteById(Long id) {
    userJpaRepository.deleteById(id);
  }

  private User toDomain(UserEntity entity) {
    return new User(entity.getId(), entity.getUsername(), entity.getPasswordHash(), entity.getRole());
  }
}
