package br.edu.pucminas.matricula.infrastructure.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SemesterJpaRepository extends JpaRepository<SemesterEntity, Long> {
  Optional<SemesterEntity> findByCode(String code);
}
