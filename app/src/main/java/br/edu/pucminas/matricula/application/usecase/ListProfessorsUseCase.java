package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.domain.model.Professor;
import br.edu.pucminas.matricula.domain.model.User;
import br.edu.pucminas.matricula.domain.repository.ProfessorRepository;
import br.edu.pucminas.matricula.domain.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListProfessorsUseCase {
  private final ProfessorRepository professorRepository;
  private final UserRepository userRepository;

  public ListProfessorsUseCase(ProfessorRepository professorRepository, UserRepository userRepository) {
    this.professorRepository = professorRepository;
    this.userRepository = userRepository;
  }

  @Transactional(readOnly = true)
  public List<ListProfessorsResponse> execute() {
    return professorRepository.findAll().stream().map(this::toResponse).toList();
  }

  private ListProfessorsResponse toResponse(Professor professor) {
    String username = userRepository.findById(professor.userId()).map(User::username).orElse("?");
    return new ListProfessorsResponse(professor.id(), professor.userId(), username, professor.name());
  }
}
