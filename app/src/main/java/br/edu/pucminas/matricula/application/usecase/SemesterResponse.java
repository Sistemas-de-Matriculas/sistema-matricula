package br.edu.pucminas.matricula.application.usecase;

import java.time.OffsetDateTime;

public record SemesterResponse(
    Long id, String code, boolean enrollmentOpen, OffsetDateTime enrollmentStart, OffsetDateTime enrollmentEnd) {}
