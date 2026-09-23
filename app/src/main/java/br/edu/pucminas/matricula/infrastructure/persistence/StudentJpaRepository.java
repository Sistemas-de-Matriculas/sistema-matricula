package br.edu.pucminas.matricula.infrastructure.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentJpaRepository extends JpaRepository<StudentEntity, Long> {
  Optional<StudentEntity> findByUser_Id(Long userId);
}
