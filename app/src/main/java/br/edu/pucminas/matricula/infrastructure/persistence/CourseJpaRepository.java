package br.edu.pucminas.matricula.infrastructure.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseJpaRepository extends JpaRepository<CourseEntity, Long> {
  Optional<CourseEntity> findByName(String name);
}
