package br.edu.pucminas.matricula.web.auth;

import br.edu.pucminas.matricula.domain.model.UserRole;

public record MeDto(Long id, String username, UserRole role) {}
