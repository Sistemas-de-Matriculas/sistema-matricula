package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.domain.model.OfferingStatus;

public record OfferingResponse(
    Long id,
    Long semesterId,
    String semesterCode,
    Long disciplineId,
    String disciplineName,
    String courseName,
    String professorName,
    int capacity,
    OfferingStatus status) {}
