package br.edu.pucminas.matricula.infrastructure.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorJpaRepository extends JpaRepository<ProfessorEntity, Long> {
  Optional<ProfessorEntity> findByUser_Id(Long userId);
}
