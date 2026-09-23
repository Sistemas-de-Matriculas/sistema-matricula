package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.domain.model.UserRole;

public record CreateUserRequest(String username, String password, UserRole role) {}
