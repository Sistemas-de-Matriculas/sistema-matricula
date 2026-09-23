package br.edu.pucminas.matricula.infrastructure.persistence;

import br.edu.pucminas.matricula.domain.model.Discipline;
import br.edu.pucminas.matricula.domain.repository.DisciplineRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class SpringDisciplineRepository implements DisciplineRepository {
  private final DisciplineJpaRepository disciplineJpaRepository;
  private final CourseJpaRepository courseJpaRepository;
  private final ProfessorJpaRepository professorJpaRepository;

  public SpringDisciplineRepository(
      DisciplineJpaRepository disciplineJpaRepository,
      CourseJpaRepository courseJpaRepository,
      ProfessorJpaRepository professorJpaRepository) {
    this.disciplineJpaRepository = disciplineJpaRepository;
    this.courseJpaRepository = courseJpaRepository;
    this.professorJpaRepository = professorJpaRepository;
  }

  @Override
  public Optional<Discipline> findById(Long id) {
    return disciplineJpaRepository.findById(id).map(this::toDomain);
  }

  @Override
  public List<Discipline> findAll() {
    return disciplineJpaRepository.findAll().stream().map(this::toDomain).toList();
  }

  @Override
  public Discipline save(Discipline discipline) {
    CourseEntity course = courseJpaRepository.findById(discipline.courseId()).orElseThrow();
    ProfessorEntity professor = professorJpaRepository.findById(discipline.professorId()).orElseThrow();
    DisciplineEntity saved = disciplineJpaRepository.save(
        new DisciplineEntity(discipline.name(), course, professor, discipline.category()));
    return toDomain(saved);
  }

  @Override
  public void deleteById(Long id) {
    disciplineJpaRepository.deleteById(id);
  }

  private Discipline toDomain(DisciplineEntity entity) {
    return new Discipline(
        entity.getId(),
        entity.getName(),
        entity.getCourse().getId(),
        entity.getProfessor().getId(),
        entity.getCategory());
  }
}
