package br.edu.pucminas.matricula.web.professor;

import br.edu.pucminas.matricula.application.usecase.EnrolledStudentResponse;
import br.edu.pucminas.matricula.application.usecase.ListOfferingStudentsUseCase;
import br.edu.pucminas.matricula.application.usecase.ListProfessorOfferingsUseCase;
import br.edu.pucminas.matricula.application.usecase.ProfessorOfferingResponse;
import br.edu.pucminas.matricula.web.security.AppUserPrincipal;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/professor/offerings")
@PreAuthorize("hasRole('PROFESSOR')")
public class ProfessorOfferingController {
  private final ListProfessorOfferingsUseCase listProfessorOfferingsUseCase;
  private final ListOfferingStudentsUseCase listOfferingStudentsUseCase;

  public ProfessorOfferingController(
      ListProfessorOfferingsUseCase listProfessorOfferingsUseCase,
      ListOfferingStudentsUseCase listOfferingStudentsUseCase) {
    this.listProfessorOfferingsUseCase = listProfessorOfferingsUseCase;
    this.listOfferingStudentsUseCase = listOfferingStudentsUseCase;
  }

  @GetMapping
  public List<ProfessorOfferingResponse> list(@AuthenticationPrincipal AppUserPrincipal principal) {
    return listProfessorOfferingsUseCase.execute(principal.id());
  }

  @GetMapping("/{id}/students")
  public List<EnrolledStudentResponse> listStudents(
      @PathVariable("id") Long offeringId, @AuthenticationPrincipal AppUserPrincipal principal) {
    return listOfferingStudentsUseCase.execute(offeringId, principal.id());
  }
}
