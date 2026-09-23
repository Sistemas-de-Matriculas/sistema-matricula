package br.edu.pucminas.matricula.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "semester")
public class SemesterEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "code", nullable = false, unique = true, length = 30)
  private String code;

  @Column(name = "enrollment_open", nullable = false)
  private boolean enrollmentOpen;

  @Column(name = "enrollment_start")
  private OffsetDateTime enrollmentStart;

  @Column(name = "enrollment_end")
  private OffsetDateTime enrollmentEnd;

  protected SemesterEntity() {}

  public SemesterEntity(
      String code, boolean enrollmentOpen, OffsetDateTime enrollmentStart, OffsetDateTime enrollmentEnd) {
    this.code = code;
    this.enrollmentOpen = enrollmentOpen;
    this.enrollmentStart = enrollmentStart;
    this.enrollmentEnd = enrollmentEnd;
  }

  public Long getId() {
    return id;
  }

  public String getCode() {
    return code;
  }

  public boolean isEnrollmentOpen() {
    return enrollmentOpen;
  }

  public OffsetDateTime getEnrollmentStart() {
    return enrollmentStart;
  }

  public OffsetDateTime getEnrollmentEnd() {
    return enrollmentEnd;
  }

  public void setEnrollmentOpen(boolean enrollmentOpen) {
    this.enrollmentOpen = enrollmentOpen;
  }

  public void setEnrollmentStart(OffsetDateTime enrollmentStart) {
    this.enrollmentStart = enrollmentStart;
  }

  public void setEnrollmentEnd(OffsetDateTime enrollmentEnd) {
    this.enrollmentEnd = enrollmentEnd;
  }
}
