package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.domain.model.UserRole;

public record ListUsersResponse(Long id, String username, UserRole role) {}
