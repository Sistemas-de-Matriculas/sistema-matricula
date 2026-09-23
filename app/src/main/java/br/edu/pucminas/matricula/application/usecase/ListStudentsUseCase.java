package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.domain.model.Student;
import br.edu.pucminas.matricula.domain.model.User;
import br.edu.pucminas.matricula.domain.repository.StudentRepository;
import br.edu.pucminas.matricula.domain.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListStudentsUseCase {
  private final StudentRepository studentRepository;
  private final UserRepository userRepository;

  public ListStudentsUseCase(StudentRepository studentRepository, UserRepository userRepository) {
    this.studentRepository = studentRepository;
    this.userRepository = userRepository;
  }

  @Transactional(readOnly = true)
  public List<ListStudentsResponse> execute() {
    return studentRepository.findAll().stream().map(this::toResponse).toList();
  }

  private ListStudentsResponse toResponse(Student student) {
    String username = userRepository.findById(student.userId()).map(User::username).orElse("?");
    return new ListStudentsResponse(student.id(), student.userId(), username, student.name());
  }
}
