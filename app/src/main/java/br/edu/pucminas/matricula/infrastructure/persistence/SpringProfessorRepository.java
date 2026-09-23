package br.edu.pucminas.matricula.infrastructure.persistence;

import br.edu.pucminas.matricula.domain.model.Professor;
import br.edu.pucminas.matricula.domain.repository.ProfessorRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class SpringProfessorRepository implements ProfessorRepository {
  private final ProfessorJpaRepository professorJpaRepository;
  private final UserJpaRepository userJpaRepository;

  public SpringProfessorRepository(
      ProfessorJpaRepository professorJpaRepository, UserJpaRepository userJpaRepository) {
    this.professorJpaRepository = professorJpaRepository;
    this.userJpaRepository = userJpaRepository;
  }

  @Override
  public Optional<Professor> findById(Long id) {
    return professorJpaRepository.findById(id).map(this::toDomain);
  }

  @Override
  public Optional<Professor> findByUserId(Long userId) {
    return professorJpaRepository.findByUser_Id(userId).map(this::toDomain);
  }

  @Override
  public List<Professor> findAll() {
    return professorJpaRepository.findAll().stream().map(this::toDomain).toList();
  }

  @Override
  public Professor save(Professor professor) {
    UserEntity user = userJpaRepository.findById(professor.userId()).orElseThrow();
    ProfessorEntity saved = professorJpaRepository.save(new ProfessorEntity(user, professor.name()));
    return toDomain(saved);
  }

  @Override
  public void deleteById(Long id) {
    professorJpaRepository.deleteById(id);
  }

  private Professor toDomain(ProfessorEntity entity) {
    return new Professor(entity.getId(), entity.getUser().getId(), entity.getName());
  }
}
