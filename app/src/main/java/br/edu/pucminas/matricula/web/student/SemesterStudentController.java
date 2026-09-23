package br.edu.pucminas.matricula.web.student;

import br.edu.pucminas.matricula.application.usecase.ListOfferingAvailabilityUseCase;
import br.edu.pucminas.matricula.application.usecase.ListOpenSemestersUseCase;
import br.edu.pucminas.matricula.application.usecase.OfferingAvailabilityResponse;
import br.edu.pucminas.matricula.application.usecase.SemesterResponse;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student/semesters")
@PreAuthorize("hasRole('ALUNO')")
public class SemesterStudentController {
  private final ListOpenSemestersUseCase listOpenSemestersUseCase;
  private final ListOfferingAvailabilityUseCase listOfferingAvailabilityUseCase;

  public SemesterStudentController(
      ListOpenSemestersUseCase listOpenSemestersUseCase,
      ListOfferingAvailabilityUseCase listOfferingAvailabilityUseCase) {
    this.listOpenSemestersUseCase = listOpenSemestersUseCase;
    this.listOfferingAvailabilityUseCase = listOfferingAvailabilityUseCase;
  }

  @GetMapping
  public List<SemesterResponse> listOpen() {
    return listOpenSemestersUseCase.execute();
  }

  @GetMapping("/{id}/offerings")
  public List<OfferingAvailabilityResponse> listOfferings(@PathVariable("id") Long semesterId) {
    return listOfferingAvailabilityUseCase.execute(semesterId);
  }
}
