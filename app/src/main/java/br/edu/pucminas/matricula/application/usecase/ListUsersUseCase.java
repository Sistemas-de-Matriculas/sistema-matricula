package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.domain.model.User;
import br.edu.pucminas.matricula.domain.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListUsersUseCase {
  private final UserRepository userRepository;

  public ListUsersUseCase(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional(readOnly = true)
  public List<ListUsersResponse> execute() {
    return userRepository.findAll().stream()
        .map(u -> new ListUsersResponse(u.id(), u.username(), u.role()))
        .toList();
  }
}
