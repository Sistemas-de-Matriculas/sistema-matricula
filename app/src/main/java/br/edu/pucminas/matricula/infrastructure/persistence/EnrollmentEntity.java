package br.edu.pucminas.matricula.infrastructure.persistence;

import br.edu.pucminas.matricula.domain.model.EnrollmentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "enrollment")
public class EnrollmentEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "offering_id", nullable = false)
  private OfferingEntity offering;

  @ManyToOne(optional = false)
  @JoinColumn(name = "student_id", nullable = false)
  private StudentEntity student;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  private EnrollmentStatus status;

  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt;

  @Column(name = "cancelled_at")
  private OffsetDateTime cancelledAt;

  protected EnrollmentEntity() {}

  public EnrollmentEntity(
      OfferingEntity offering,
      StudentEntity student,
      EnrollmentStatus status,
      OffsetDateTime createdAt,
      OffsetDateTime cancelledAt) {
    this.offering = offering;
    this.student = student;
    this.status = status;
    this.createdAt = createdAt;
    this.cancelledAt = cancelledAt;
  }

  public Long getId() {
    return id;
  }

  public OfferingEntity getOffering() {
    return offering;
  }

  public StudentEntity getStudent() {
    return student;
  }

  public EnrollmentStatus getStatus() {
    return status;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public OffsetDateTime getCancelledAt() {
    return cancelledAt;
  }

  public void setStatus(EnrollmentStatus status) {
    this.status = status;
  }

  public void setCancelledAt(OffsetDateTime cancelledAt) {
    this.cancelledAt = cancelledAt;
  }
}
