package br.edu.pucminas.matricula.infrastructure.persistence;

import br.edu.pucminas.matricula.domain.model.Semester;
import br.edu.pucminas.matricula.domain.repository.SemesterRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class SpringSemesterRepository implements SemesterRepository {
  private final SemesterJpaRepository semesterJpaRepository;

  public SpringSemesterRepository(SemesterJpaRepository semesterJpaRepository) {
    this.semesterJpaRepository = semesterJpaRepository;
  }

  @Override
  public Optional<Semester> findById(Long id) {
    return semesterJpaRepository.findById(id).map(this::toDomain);
  }

  @Override
  public Optional<Semester> findByCode(String code) {
    return semesterJpaRepository.findByCode(code).map(this::toDomain);
  }

  @Override
  public List<Semester> findAll() {
    return semesterJpaRepository.findAll().stream().map(this::toDomain).toList();
  }

  @Override
  public Semester save(Semester semester) {
    SemesterEntity saved =
        semesterJpaRepository.save(
            new SemesterEntity(
                semester.code(),
                semester.enrollmentOpen(),
                semester.enrollmentStart(),
                semester.enrollmentEnd()));
    return toDomain(saved);
  }

  @Override
  public Semester update(Semester semester) {
    SemesterEntity entity = semesterJpaRepository.findById(semester.id()).orElseThrow();
    entity.setEnrollmentOpen(semester.enrollmentOpen());
    entity.setEnrollmentStart(semester.enrollmentStart());
    entity.setEnrollmentEnd(semester.enrollmentEnd());
    SemesterEntity saved = semesterJpaRepository.save(entity);
    return toDomain(saved);
  }

  private Semester toDomain(SemesterEntity entity) {
    return new Semester(
        entity.getId(),
        entity.getCode(),
        entity.isEnrollmentOpen(),
        entity.getEnrollmentStart(),
        entity.getEnrollmentEnd());
  }
}
