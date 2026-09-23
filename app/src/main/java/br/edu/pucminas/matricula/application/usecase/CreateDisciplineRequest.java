package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.domain.model.DisciplineCategory;

public record CreateDisciplineRequest(String name, Long courseId, Long professorId, DisciplineCategory category) {}
