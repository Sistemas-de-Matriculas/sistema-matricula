package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.domain.exception.ValidationException;
import br.edu.pucminas.matricula.domain.model.Professor;
import br.edu.pucminas.matricula.domain.repository.ProfessorRepository;
import br.edu.pucminas.matricula.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteProfessorUseCase {
  private final ProfessorRepository professorRepository;
  private final UserRepository userRepository;

  public DeleteProfessorUseCase(ProfessorRepository professorRepository, UserRepository userRepository) {
    this.professorRepository = professorRepository;
    this.userRepository = userRepository;
  }

  @Transactional
  public void execute(Long professorId) {
    if (professorId == null) {
      throw new ValidationException("ID inválido.");
    }

    Professor professor = professorRepository.findById(professorId)
        .orElseThrow(() -> new ValidationException("Professor não encontrado."));
    professorRepository.deleteById(professorId);
    userRepository.deleteById(professor.userId());
  }
}
