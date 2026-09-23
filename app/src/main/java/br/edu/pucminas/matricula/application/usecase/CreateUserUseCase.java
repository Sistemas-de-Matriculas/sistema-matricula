package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.application.security.PasswordHasher;
import br.edu.pucminas.matricula.domain.exception.ValidationException;
import br.edu.pucminas.matricula.domain.model.User;
import br.edu.pucminas.matricula.domain.model.UserRole;
import br.edu.pucminas.matricula.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateUserUseCase {
  private final UserRepository userRepository;
  private final PasswordHasher passwordHasher;

  public CreateUserUseCase(UserRepository userRepository, PasswordHasher passwordHasher) {
    this.userRepository = userRepository;
    this.passwordHasher = passwordHasher;
  }

  @Transactional
  public CreateUserResponse execute(CreateUserRequest request) {
    if (request == null) {
      throw new ValidationException("Requisição inválida.");
    }
    String username = normalize(request.username());
    if (username.isEmpty()) {
      throw new ValidationException("Login é obrigatório.");
    }
    if (username.length() > 50) {
      throw new ValidationException("Login deve ter no máximo 50 caracteres.");
    }
    if (request.password() == null || request.password().trim().isEmpty()) {
      throw new ValidationException("Senha é obrigatória.");
    }
    if (request.role() == null) {
      throw new ValidationException("Perfil é obrigatório.");
    }

    userRepository.findByUsername(username).ifPresent(u -> {
      throw new ValidationException("Já existe um usuário com esse login.");
    });

    UserRole role = request.role();
    User saved = userRepository.save(new User(null, username, passwordHasher.hash(request.password()), role));
    return new CreateUserResponse(saved.id(), saved.username(), saved.role());
  }

  private String normalize(String value) {
    if (value == null) {
      return "";
    }
    return value.trim();
  }
}
