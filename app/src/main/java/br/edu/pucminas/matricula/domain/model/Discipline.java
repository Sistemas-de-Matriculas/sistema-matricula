package br.edu.pucminas.matricula.domain.model;

public record Discipline(Long id, String name, Long courseId, Long professorId, DisciplineCategory category) {}
