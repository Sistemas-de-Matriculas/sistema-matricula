package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.domain.model.DisciplineCategory;

public record DisciplineResponse(
    Long id,
    String name,
    Long courseId,
    String courseName,
    Long professorId,
    String professorName,
    DisciplineCategory category) {}
