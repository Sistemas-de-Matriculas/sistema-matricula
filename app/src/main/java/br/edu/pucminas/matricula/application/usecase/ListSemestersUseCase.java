package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.domain.repository.SemesterRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListSemestersUseCase {
  private final SemesterRepository semesterRepository;

  public ListSemestersUseCase(SemesterRepository semesterRepository) {
    this.semesterRepository = semesterRepository;
  }

  @Transactional(readOnly = true)
  public List<SemesterResponse> execute() {
    return semesterRepository.findAll().stream()
        .map(s -> new SemesterResponse(s.id(), s.code(), s.enrollmentOpen(), s.enrollmentStart(), s.enrollmentEnd()))
        .toList();
  }
}
