package br.edu.pucminas.matricula.domain.model;

public record User(Long id, String username, String passwordHash, UserRole role) {}
