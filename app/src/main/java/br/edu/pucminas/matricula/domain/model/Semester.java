package br.edu.pucminas.matricula.domain.model;

import java.time.OffsetDateTime;

public record Semester(
    Long id, String code, boolean enrollmentOpen, OffsetDateTime enrollmentStart, OffsetDateTime enrollmentEnd) {}
