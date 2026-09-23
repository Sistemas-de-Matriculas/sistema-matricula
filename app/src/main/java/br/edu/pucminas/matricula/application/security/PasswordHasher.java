package br.edu.pucminas.matricula.application.security;

public interface PasswordHasher {
  String hash(String rawPassword);

  boolean matches(String rawPassword, String passwordHash);
}
