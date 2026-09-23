package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.domain.exception.ValidationException;
import br.edu.pucminas.matricula.domain.model.Student;
import br.edu.pucminas.matricula.domain.repository.StudentRepository;
import br.edu.pucminas.matricula.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteStudentUseCase {
  private final StudentRepository studentRepository;
  private final UserRepository userRepository;

  public DeleteStudentUseCase(StudentRepository studentRepository, UserRepository userRepository) {
    this.studentRepository = studentRepository;
    this.userRepository = userRepository;
  }

  @Transactional
  public void execute(Long studentId) {
    if (studentId == null) {
      throw new ValidationException("ID inválido.");
    }

    Student student = studentRepository.findById(studentId).orElseThrow(() -> new ValidationException("Aluno não encontrado."));
    studentRepository.deleteById(studentId);
    userRepository.deleteById(student.userId());
  }
}
