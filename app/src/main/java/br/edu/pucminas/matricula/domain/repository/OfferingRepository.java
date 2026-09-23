package br.edu.pucminas.matricula.domain.repository;

import br.edu.pucminas.matricula.domain.model.Offering;
import java.util.List;
import java.util.Optional;

public interface OfferingRepository {
  Optional<Offering> findById(Long id);

  List<Offering> findBySemesterId(Long semesterId);

  Offering save(Offering offering);

  void deleteById(Long id);
}
