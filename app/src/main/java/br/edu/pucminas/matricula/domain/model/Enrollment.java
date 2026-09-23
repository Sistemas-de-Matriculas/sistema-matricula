package br.edu.pucminas.matricula.domain.model;

import java.time.OffsetDateTime;

public record Enrollment(
    Long id,
    Long offeringId,
    Long studentId,
    EnrollmentStatus status,
    OffsetDateTime createdAt,
    OffsetDateTime cancelledAt) {}
