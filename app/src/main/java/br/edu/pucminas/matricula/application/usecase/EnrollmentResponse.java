package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.domain.model.DisciplineCategory;
import br.edu.pucminas.matricula.domain.model.EnrollmentStatus;
import java.time.OffsetDateTime;

public record EnrollmentResponse(
    Long id,
    Long semesterId,
    String semesterCode,
    Long offeringId,
    Long disciplineId,
    String disciplineName,
    DisciplineCategory disciplineCategory,
    EnrollmentStatus status,
    OffsetDateTime createdAt) {}
