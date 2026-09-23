package br.edu.pucminas.matricula.web.student;

import br.edu.pucminas.matricula.application.usecase.CancelStudentEnrollmentUseCase;
import br.edu.pucminas.matricula.application.usecase.EnrollStudentRequest;
import br.edu.pucminas.matricula.application.usecase.EnrollStudentUseCase;
import br.edu.pucminas.matricula.application.usecase.EnrollmentResponse;
import br.edu.pucminas.matricula.application.usecase.ListStudentEnrollmentsUseCase;
import br.edu.pucminas.matricula.web.security.AppUserPrincipal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student")
@PreAuthorize("hasRole('ALUNO')")
public class EnrollmentStudentController {
  private final EnrollStudentUseCase enrollStudentUseCase;
  private final ListStudentEnrollmentsUseCase listStudentEnrollmentsUseCase;
  private final CancelStudentEnrollmentUseCase cancelStudentEnrollmentUseCase;

  public EnrollmentStudentController(
      EnrollStudentUseCase enrollStudentUseCase,
      ListStudentEnrollmentsUseCase listStudentEnrollmentsUseCase,
      CancelStudentEnrollmentUseCase cancelStudentEnrollmentUseCase) {
    this.enrollStudentUseCase = enrollStudentUseCase;
    this.listStudentEnrollmentsUseCase = listStudentEnrollmentsUseCase;
    this.cancelStudentEnrollmentUseCase = cancelStudentEnrollmentUseCase;
  }

  @PostMapping("/semesters/{semesterId}/enrollments")
  @ResponseStatus(HttpStatus.CREATED)
  public List<EnrollmentResponse> enroll(
      @PathVariable("semesterId") Long semesterId,
      @RequestBody EnrollStudentRequestBody body,
      @AuthenticationPrincipal AppUserPrincipal principal) {
    List<Long> offeringIds = body != null && body.offeringIds() != null ? body.offeringIds() : List.of();
    return enrollStudentUseCase.execute(new EnrollStudentRequest(semesterId, offeringIds), principal.id());
  }

  @GetMapping("/semesters/{semesterId}/enrollments")
  public List<EnrollmentResponse> list(
      @PathVariable("semesterId") Long semesterId,
      @AuthenticationPrincipal AppUserPrincipal principal) {
    return listStudentEnrollmentsUseCase.execute(semesterId, principal.id());
  }

  @DeleteMapping("/enrollments/{enrollmentId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void cancel(
      @PathVariable("enrollmentId") Long enrollmentId,
      @AuthenticationPrincipal AppUserPrincipal principal) {
    cancelStudentEnrollmentUseCase.execute(enrollmentId, principal.id());
  }

  public record EnrollStudentRequestBody(List<Long> offeringIds) {}
}
