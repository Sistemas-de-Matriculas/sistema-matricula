package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.domain.exception.ValidationException;
import br.edu.pucminas.matricula.domain.repository.DisciplineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteDisciplineUseCase {
  private final DisciplineRepository disciplineRepository;

  public DeleteDisciplineUseCase(DisciplineRepository disciplineRepository) {
    this.disciplineRepository = disciplineRepository;
  }

  @Transactional
  public void execute(Long disciplineId) {
    if (disciplineId == null) {
      throw new ValidationException("ID inválido.");
    }
    if (disciplineRepository.findById(disciplineId).isEmpty()) {
      throw new ValidationException("Disciplina não encontrada.");
    }
    disciplineRepository.deleteById(disciplineId);
  }
}
