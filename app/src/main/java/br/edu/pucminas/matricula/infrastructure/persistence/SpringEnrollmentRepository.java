package br.edu.pucminas.matricula.infrastructure.persistence;

import br.edu.pucminas.matricula.domain.model.Enrollment;
import br.edu.pucminas.matricula.domain.model.EnrollmentStatus;
import br.edu.pucminas.matricula.domain.repository.EnrollmentRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class SpringEnrollmentRepository implements EnrollmentRepository {
  private final EnrollmentJpaRepository enrollmentJpaRepository;
  private final OfferingJpaRepository offeringJpaRepository;
  private final StudentJpaRepository studentJpaRepository;

  public SpringEnrollmentRepository(
      EnrollmentJpaRepository enrollmentJpaRepository,
      OfferingJpaRepository offeringJpaRepository,
      StudentJpaRepository studentJpaRepository) {
    this.enrollmentJpaRepository = enrollmentJpaRepository;
    this.offeringJpaRepository = offeringJpaRepository;
    this.studentJpaRepository = studentJpaRepository;
  }

  @Override
  public long countEnrolledByOfferingId(Long offeringId) {
    return enrollmentJpaRepository.countByOffering_IdAndStatus(offeringId, EnrollmentStatus.ENROLLED);
  }

  @Override
  public boolean existsActiveEnrollment(Long offeringId, Long studentId) {
    return enrollmentJpaRepository.existsByOffering_IdAndStudent_IdAndStatus(
        offeringId, studentId, EnrollmentStatus.ENROLLED);
  }

  @Override
  public Enrollment save(Enrollment enrollment) {
    if (enrollment.id() != null) {
      EnrollmentEntity existing = enrollmentJpaRepository.findById(enrollment.id()).orElseThrow();
      existing.setStatus(enrollment.status());
      existing.setCancelledAt(enrollment.cancelledAt());
      return toDomain(enrollmentJpaRepository.save(existing));
    }

    OfferingEntity offering = offeringJpaRepository.findById(enrollment.offeringId()).orElseThrow();
    StudentEntity student = studentJpaRepository.findById(enrollment.studentId()).orElseThrow();

    OffsetDateTime createdAt =
        enrollment.createdAt() != null ? enrollment.createdAt() : OffsetDateTime.now();

    EnrollmentEntity entity = new EnrollmentEntity(
        offering,
        student,
        enrollment.status(),
        createdAt,
        enrollment.cancelledAt());
    EnrollmentEntity saved = enrollmentJpaRepository.save(entity);
    return toDomain(saved);
  }

  @Override
  public Optional<Enrollment> findById(Long id) {
    return enrollmentJpaRepository.findById(id).map(this::toDomain);
  }

  @Override
  public List<Enrollment> findByStudentIdAndSemesterId(Long studentId, Long semesterId) {
    return enrollmentJpaRepository
        .findByStudent_IdAndOffering_Semester_Id(studentId, semesterId)
        .stream()
        .map(this::toDomain)
        .toList();
  }

  @Override
  public List<Enrollment> findActiveByOfferingId(Long offeringId) {
    return enrollmentJpaRepository
        .findByOffering_IdAndStatus(offeringId, EnrollmentStatus.ENROLLED)
        .stream()
        .map(this::toDomain)
        .toList();
  }

  private Enrollment toDomain(EnrollmentEntity entity) {
    return new Enrollment(
        entity.getId(),
        entity.getOffering().getId(),
        entity.getStudent().getId(),
        entity.getStatus(),
        entity.getCreatedAt(),
        entity.getCancelledAt());
  }
}
