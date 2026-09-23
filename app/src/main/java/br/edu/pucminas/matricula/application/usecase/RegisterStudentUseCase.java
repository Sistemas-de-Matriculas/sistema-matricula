package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.application.security.PasswordHasher;
import br.edu.pucminas.matricula.domain.exception.ValidationException;
import br.edu.pucminas.matricula.domain.model.Student;
import br.edu.pucminas.matricula.domain.model.User;
import br.edu.pucminas.matricula.domain.model.UserRole;
import br.edu.pucminas.matricula.domain.repository.StudentRepository;
import br.edu.pucminas.matricula.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterStudentUseCase {
  private final UserRepository userRepository;
  private final StudentRepository studentRepository;
  private final PasswordHasher passwordHasher;

  public RegisterStudentUseCase(
      UserRepository userRepository,
      StudentRepository studentRepository,
      PasswordHasher passwordHasher) {
    this.userRepository = userRepository;
    this.studentRepository = studentRepository;
    this.passwordHasher = passwordHasher;
  }

  @Transactional
  public RegisterStudentResponse execute(RegisterStudentRequest request) {
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
        new User(null, username, passwordHasher.hash(request.password()), UserRole.ALUNO));
    Student student = studentRepository.save(new Student(null, user.id(), name));
    return new RegisterStudentResponse(student.id(), user.id(), user.username(), student.name());
  }

  private String normalize(String value) {
    if (value == null) {
      return "";
    }
    return value.trim();
  }
}
