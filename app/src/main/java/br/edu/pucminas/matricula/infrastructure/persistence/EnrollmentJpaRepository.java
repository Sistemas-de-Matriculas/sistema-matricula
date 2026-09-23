package br.edu.pucminas.matricula.infrastructure.persistence;

import br.edu.pucminas.matricula.domain.model.EnrollmentStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentJpaRepository extends JpaRepository<EnrollmentEntity, Long> {
  long countByOffering_IdAndStatus(Long offeringId, EnrollmentStatus status);

  boolean existsByOffering_IdAndStudent_IdAndStatus(Long offeringId, Long studentId, EnrollmentStatus status);

  List<EnrollmentEntity> findByStudent_IdAndOffering_Semester_Id(Long studentId, Long semesterId);
}
