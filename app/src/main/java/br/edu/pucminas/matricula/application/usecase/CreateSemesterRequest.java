package br.edu.pucminas.matricula.application.usecase;

import java.time.OffsetDateTime;

public record CreateSemesterRequest(String code, OffsetDateTime enrollmentEnd) {}
