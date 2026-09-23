package br.edu.pucminas.matricula.web.admin;

import br.edu.pucminas.matricula.application.usecase.AddOfferingRequest;
import br.edu.pucminas.matricula.application.usecase.AddOfferingUseCase;
import br.edu.pucminas.matricula.application.usecase.CreateSemesterRequest;
import br.edu.pucminas.matricula.application.usecase.CreateSemesterUseCase;
import br.edu.pucminas.matricula.application.usecase.ListOfferingsUseCase;
import br.edu.pucminas.matricula.application.usecase.ListSemestersUseCase;
import br.edu.pucminas.matricula.application.usecase.OfferingResponse;
import br.edu.pucminas.matricula.application.usecase.SemesterResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/semesters")
@PreAuthorize("hasRole('SECRETARIA')")
public class SemesterAdminController {
  private final CreateSemesterUseCase createSemesterUseCase;
  private final ListSemestersUseCase listSemestersUseCase;
  private final AddOfferingUseCase addOfferingUseCase;
  private final ListOfferingsUseCase listOfferingsUseCase;

  public SemesterAdminController(
      CreateSemesterUseCase createSemesterUseCase,
      ListSemestersUseCase listSemestersUseCase,
      AddOfferingUseCase addOfferingUseCase,
      ListOfferingsUseCase listOfferingsUseCase) {
    this.createSemesterUseCase = createSemesterUseCase;
    this.listSemestersUseCase = listSemestersUseCase;
    this.addOfferingUseCase = addOfferingUseCase;
    this.listOfferingsUseCase = listOfferingsUseCase;
  }

  @GetMapping
  public List<SemesterResponse> list() {
    return listSemestersUseCase.execute();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public SemesterResponse create(@RequestBody CreateSemesterRequest request) {
    return createSemesterUseCase.execute(request);
  }

  @GetMapping("/{id}/offerings")
  public List<OfferingResponse> listOfferings(@PathVariable("id") Long semesterId) {
    return listOfferingsUseCase.execute(semesterId);
  }

  @PostMapping("/{id}/offerings")
  @ResponseStatus(HttpStatus.CREATED)
  public OfferingResponse addOffering(
      @PathVariable("id") Long semesterId, @RequestBody AddOfferingToSemesterRequest request) {
    return addOfferingUseCase.execute(new AddOfferingRequest(semesterId, request.disciplineId()));
  }

  public record AddOfferingToSemesterRequest(Long disciplineId) {}
}

