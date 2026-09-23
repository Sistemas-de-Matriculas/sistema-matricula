package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.application.security.PasswordHasher;
import br.edu.pucminas.matricula.domain.model.User;
import br.edu.pucminas.matricula.domain.model.UserRole;
import br.edu.pucminas.matricula.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BootstrapAdminUser {
  private final UserRepository userRepository;
  private final PasswordHasher passwordHasher;
  private final String adminUsername;
  private final String adminPassword;

  public BootstrapAdminUser(
      UserRepository userRepository,
      PasswordHasher passwordHasher,
      @Value("${bootstrap.admin.username:secretaria}") String adminUsername,
      @Value("${bootstrap.admin.password:secretaria}") String adminPassword) {
    this.userRepository = userRepository;
    this.passwordHasher = passwordHasher;
    this.adminUsername = adminUsername;
    this.adminPassword = adminPassword;
  }

  @Transactional
  public void executeIfEmpty() {
    if (userRepository.existsAny()) {
      return;
    }

    userRepository.save(new User(null, adminUsername, passwordHasher.hash(adminPassword), UserRole.SECRETARIA));
    System.out.println();
    System.out.println("Usuário inicial criado.");
    System.out.println("Login: " + adminUsername);
    System.out.println("A senha inicial foi configurada via BOOTSTRAP_ADMIN_PASSWORD (ou valor padrão).");
  }
}
