package br.edu.pucminas.matricula.infrastructure.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferingJpaRepository extends JpaRepository<OfferingEntity, Long> {
  List<OfferingEntity> findBySemester_Id(Long semesterId);
}
