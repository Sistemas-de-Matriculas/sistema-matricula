package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.domain.model.DisciplineCategory;
import br.edu.pucminas.matricula.domain.model.OfferingStatus;

public record OfferingAvailabilityResponse(
    Long offeringId,
    Long semesterId,
    String semesterCode,
    Long disciplineId,
    String disciplineName,
    String courseName,
    String professorName,
    int capacity,
    long enrolledCount,
    long availableSeats,
    OfferingStatus status,
    DisciplineCategory category) {}
