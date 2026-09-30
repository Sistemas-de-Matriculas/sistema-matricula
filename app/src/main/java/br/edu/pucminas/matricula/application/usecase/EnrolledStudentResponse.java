package br.edu.pucminas.matricula.application.usecase;

import java.time.OffsetDateTime;

public record EnrolledStudentResponse(
    Long enrollmentId, Long studentId, String studentName, String username, OffsetDateTime enrolledAt) {}
