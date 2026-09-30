package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.domain.model.DisciplineCategory;
import br.edu.pucminas.matricula.domain.model.OfferingStatus;

public record ProfessorOfferingResponse(
    Long offeringId,
    Long semesterId,
    String semesterCode,
    Long disciplineId,
    String disciplineName,
    String courseName,
    DisciplineCategory category,
    int capacity,
    long enrolledCount,
    OfferingStatus status) {}
