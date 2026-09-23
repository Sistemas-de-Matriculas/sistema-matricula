package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.domain.model.Semester;
import br.edu.pucminas.matricula.domain.repository.SemesterRepository;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListOpenSemestersUseCase {
  private final SemesterRepository semesterRepository;

  public ListOpenSemestersUseCase(SemesterRepository semesterRepository) {
    this.semesterRepository = semesterRepository;
  }

  @Transactional(readOnly = true)
  public List<SemesterResponse> execute() {
    OffsetDateTime now = OffsetDateTime.now();

    return semesterRepository.findAll().stream()
        .filter(s -> isEnrollmentPeriodOpen(s, now))
        .sorted(Comparator.comparing(Semester::code))
        .map(s -> new SemesterResponse(s.id(), s.code(), s.enrollmentOpen(), s.enrollmentStart(), s.enrollmentEnd()))
        .toList();
  }

  private boolean isEnrollmentPeriodOpen(Semester semester, OffsetDateTime now) {
    if (!semester.enrollmentOpen()) {
      return false;
    }
    if (semester.enrollmentStart() != null && now.isBefore(semester.enrollmentStart())) {
      return false;
    }
    if (semester.enrollmentEnd() != null && !now.isBefore(semester.enrollmentEnd())) {
      return false;
    }
    return true;
  }
}
