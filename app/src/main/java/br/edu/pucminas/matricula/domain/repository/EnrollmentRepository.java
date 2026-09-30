package br.edu.pucminas.matricula.domain.repository;

import br.edu.pucminas.matricula.domain.model.Enrollment;
import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository {
  long countEnrolledByOfferingId(Long offeringId);

  boolean existsActiveEnrollment(Long offeringId, Long studentId);

  Enrollment save(Enrollment enrollment);

  Optional<Enrollment> findById(Long id);

  List<Enrollment> findByStudentIdAndSemesterId(Long studentId, Long semesterId);

  List<Enrollment> findActiveByOfferingId(Long offeringId);
}
