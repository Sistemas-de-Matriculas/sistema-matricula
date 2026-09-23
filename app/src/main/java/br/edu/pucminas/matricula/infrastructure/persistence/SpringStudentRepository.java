package br.edu.pucminas.matricula.infrastructure.persistence;

import br.edu.pucminas.matricula.domain.model.Student;
import br.edu.pucminas.matricula.domain.repository.StudentRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class SpringStudentRepository implements StudentRepository {
  private final StudentJpaRepository studentJpaRepository;
  private final UserJpaRepository userJpaRepository;

  public SpringStudentRepository(StudentJpaRepository studentJpaRepository, UserJpaRepository userJpaRepository) {
    this.studentJpaRepository = studentJpaRepository;
    this.userJpaRepository = userJpaRepository;
  }

  @Override
  public Optional<Student> findById(Long id) {
    return studentJpaRepository.findById(id).map(this::toDomain);
  }

  @Override
  public Optional<Student> findByUserId(Long userId) {
    return studentJpaRepository.findByUser_Id(userId).map(this::toDomain);
  }

  @Override
  public List<Student> findAll() {
    return studentJpaRepository.findAll().stream().map(this::toDomain).toList();
  }

  @Override
  public Student save(Student student) {
    UserEntity user = userJpaRepository.findById(student.userId()).orElseThrow();
    StudentEntity saved = studentJpaRepository.save(new StudentEntity(user, student.name()));
    return toDomain(saved);
  }

  @Override
  public void deleteById(Long id) {
    studentJpaRepository.deleteById(id);
  }

  private Student toDomain(StudentEntity entity) {
    return new Student(entity.getId(), entity.getUser().getId(), entity.getName());
  }
}
