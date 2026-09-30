package br.edu.pucminas.matricula.infrastructure.persistence;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OfferingJpaRepository extends JpaRepository<OfferingEntity, Long> {
  List<OfferingEntity> findBySemester_Id(Long semesterId);

  List<OfferingEntity> findByDiscipline_Id(Long disciplineId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select o from OfferingEntity o where o.id = :id")
  Optional<OfferingEntity> findByIdForUpdate(@Param("id") Long id);
}
