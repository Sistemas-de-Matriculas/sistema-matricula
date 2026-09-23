package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.application.security.PasswordHasher;
import br.edu.pucminas.matricula.domain.exception.ValidationException;
import br.edu.pucminas.matricula.domain.model.Professor;
import br.edu.pucminas.matricula.domain.model.User;
import br.edu.pucminas.matricula.domain.model.UserRole;
import br.edu.pucminas.matricula.domain.repository.ProfessorRepository;
import br.edu.pucminas.matricula.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterProfessorUseCase {
  private final UserRepository userRepository;
  private final ProfessorRepository professorRepository;
  private final PasswordHasher passwordHasher;

  public RegisterProfessorUseCase(
      UserRepository userRepository,
      ProfessorRepository professorRepository,
      PasswordHasher passwordHasher) {
    this.userRepository = userRepository;
    this.professorRepository = professorRepository;
    this.passwordHasher = passwordHasher;
  }

  @Transactional
  public RegisterProfessorResponse execute(RegisterProfessorRequest request) {
    if (request == null) {
      throw new ValidationException("Requisição inválida.");
    }

    String username = normalize(request.username());
    String name = normalize(request.name());
    if (username.isEmpty()) {
      throw new ValidationException("Login é obrigatório.");
    }
    if (username.length() > 50) {
      throw new ValidationException("Login deve ter no máximo 50 caracteres.");
    }
    if (request.password() == null || request.password().trim().isEmpty()) {
      throw new ValidationException("Senha é obrigatória.");
    }
    if (name.isEmpty()) {
      throw new ValidationException("Nome é obrigatório.");
    }
    if (name.length() > 120) {
      throw new ValidationException("Nome deve ter no máximo 120 caracteres.");
    }

    userRepository.findByUsername(username).ifPresent(u -> {
      throw new ValidationException("Já existe um usuário com esse login.");
    });

    User user = userRepository.save(
        new User(null, username, passwordHasher.hash(request.password()), UserRole.PROFESSOR));
    Professor professor = professorRepository.save(new Professor(null, user.id(), name));
    return new RegisterProfessorResponse(professor.id(), user.id(), user.username(), professor.name());
  }

  private String normalize(String value) {
    if (value == null) {
      return "";
    }
    return value.trim();
  }
}
