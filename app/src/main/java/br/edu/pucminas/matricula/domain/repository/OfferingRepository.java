package br.edu.pucminas.matricula.domain.repository;

import br.edu.pucminas.matricula.domain.model.Offering;
import java.util.List;
import java.util.Optional;

public interface OfferingRepository {
  Optional<Offering> findById(Long id);

  /** Busca a oferta bloqueando-a até o fim da transação (evita ultrapassar as 60 vagas - RNF04). */
  Optional<Offering> findByIdForUpdate(Long id);

  List<Offering> findBySemesterId(Long semesterId);

  List<Offering> findByDisciplineId(Long disciplineId);

  Offering save(Offering offering);

  void deleteById(Long id);
}
