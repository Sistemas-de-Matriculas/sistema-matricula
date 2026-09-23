package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.domain.exception.ValidationException;
import br.edu.pucminas.matricula.domain.repository.OfferingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteOfferingUseCase {
  private final OfferingRepository offeringRepository;

  public DeleteOfferingUseCase(OfferingRepository offeringRepository) {
    this.offeringRepository = offeringRepository;
  }

  @Transactional
  public void execute(Long offeringId) {
    if (offeringId == null) {
      throw new ValidationException("ID inválido.");
    }
    if (offeringRepository.findById(offeringId).isEmpty()) {
      throw new ValidationException("Oferta não encontrada.");
    }
    offeringRepository.deleteById(offeringId);
  }
}

