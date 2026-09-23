package br.edu.pucminas.matricula.infrastructure.security;

import br.edu.pucminas.matricula.application.security.PasswordHasher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordHasher implements PasswordHasher {
  private final PasswordEncoder encoder;

  public BCryptPasswordHasher(PasswordEncoder encoder) {
    this.encoder = encoder;
  }

  @Override
  public String hash(String rawPassword) {
    return encoder.encode(rawPassword);
  }

  @Override
  public boolean matches(String rawPassword, String passwordHash) {
    return encoder.matches(rawPassword, passwordHash);
  }
}
