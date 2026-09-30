package br.edu.pucminas.matricula.infrastructure.persistence;

import br.edu.pucminas.matricula.domain.model.Offering;
import br.edu.pucminas.matricula.domain.repository.OfferingRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class SpringOfferingRepository implements OfferingRepository {
  private final OfferingJpaRepository offeringJpaRepository;
  private final SemesterJpaRepository semesterJpaRepository;
  private final DisciplineJpaRepository disciplineJpaRepository;

  public SpringOfferingRepository(
      OfferingJpaRepository offeringJpaRepository,
      SemesterJpaRepository semesterJpaRepository,
      DisciplineJpaRepository disciplineJpaRepository) {
    this.offeringJpaRepository = offeringJpaRepository;
    this.semesterJpaRepository = semesterJpaRepository;
    this.disciplineJpaRepository = disciplineJpaRepository;
  }

  @Override
  public Optional<Offering> findById(Long id) {
    return offeringJpaRepository.findById(id).map(this::toDomain);
  }

  @Override
  public Optional<Offering> findByIdForUpdate(Long id) {
    return offeringJpaRepository.findByIdForUpdate(id).map(this::toDomain);
  }

  @Override
  public List<Offering> findBySemesterId(Long semesterId) {
    return offeringJpaRepository.findBySemester_Id(semesterId).stream().map(this::toDomain).toList();
  }

  @Override
  public List<Offering> findByDisciplineId(Long disciplineId) {
    return offeringJpaRepository.findByDiscipline_Id(disciplineId).stream().map(this::toDomain).toList();
  }

  @Override
  public Offering save(Offering offering) {
    SemesterEntity semester = semesterJpaRepository.findById(offering.semesterId()).orElseThrow();
    DisciplineEntity discipline = disciplineJpaRepository.findById(offering.disciplineId()).orElseThrow();
    OfferingEntity saved =
        offeringJpaRepository.save(
            new OfferingEntity(semester, discipline, offering.capacity(), offering.status()));
    return toDomain(saved);
  }

  @Override
  public void deleteById(Long id) {
    offeringJpaRepository.deleteById(id);
  }

  private Offering toDomain(OfferingEntity entity) {
    return new Offering(
        entity.getId(),
        entity.getSemester().getId(),
        entity.getDiscipline().getId(),
        entity.getCapacity(),
        entity.getStatus());
  }
}
