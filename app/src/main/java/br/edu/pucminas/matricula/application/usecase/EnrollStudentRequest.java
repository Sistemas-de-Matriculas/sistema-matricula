package br.edu.pucminas.matricula.application.usecase;

import java.util.List;

public record EnrollStudentRequest(Long semesterId, List<Long> offeringIds) {}
