package br.edu.pucminas.matricula.infrastructure.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisciplineJpaRepository extends JpaRepository<DisciplineEntity, Long> {
  List<DisciplineEntity> findByProfessor_Id(Long professorId);
}
