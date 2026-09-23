package br.edu.pucminas.matricula.domain.model;

public record Offering(
    Long id, Long semesterId, Long disciplineId, int capacity, OfferingStatus status) {}
