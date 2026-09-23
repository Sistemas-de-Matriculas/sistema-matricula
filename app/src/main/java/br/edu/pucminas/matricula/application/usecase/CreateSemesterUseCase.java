package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.domain.exception.ValidationException;
import br.edu.pucminas.matricula.domain.model.Semester;
import br.edu.pucminas.matricula.domain.repository.SemesterRepository;
import java.time.OffsetDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateSemesterUseCase {
  private final SemesterRepository semesterRepository;

  public CreateSemesterUseCase(SemesterRepository semesterRepository) {
    this.semesterRepository = semesterRepository;
  }

  @Transactional
  public SemesterResponse execute(CreateSemesterRequest request) {
    if (request == null) {
      throw new ValidationException("Requisição inválida.");
    }

    String code = normalize(request.code());
    if (code.isEmpty()) {
      throw new ValidationException("Código do semestre é obrigatório.");
    }
    if (code.length() > 30) {
      throw new ValidationException("Código do semestre deve ter no máximo 30 caracteres.");
    }

    semesterRepository.findByCode(code).ifPresent(s -> {
      throw new ValidationException("Já existe um semestre com esse código.");
    });

    OffsetDateTime start = OffsetDateTime.now();
    Semester saved =
        semesterRepository.save(new Semester(null, code, true, start, request.enrollmentEnd()));
    return new SemesterResponse(
        saved.id(), saved.code(), saved.enrollmentOpen(), saved.enrollmentStart(), saved.enrollmentEnd());
  }

  private String normalize(String value) {
    if (value == null) {
      return "";
    }
    return value.trim();
  }
}
