package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.domain.exception.ValidationException;
import br.edu.pucminas.matricula.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteUserUseCase {
  private final UserRepository userRepository;

  public DeleteUserUseCase(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional
  public void execute(Long userId) {
    if (userId == null) {
      throw new ValidationException("ID inválido.");
    }

    boolean exists = userRepository.findById(userId).isPresent();
    if (!exists) {
      throw new ValidationException("Usuário não encontrado.");
    }

    userRepository.deleteById(userId);
  }
}
