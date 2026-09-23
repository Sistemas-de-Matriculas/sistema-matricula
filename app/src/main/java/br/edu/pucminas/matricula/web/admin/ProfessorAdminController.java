package br.edu.pucminas.matricula.web.admin;

import br.edu.pucminas.matricula.application.usecase.DeleteProfessorUseCase;
import br.edu.pucminas.matricula.application.usecase.ListProfessorsResponse;
import br.edu.pucminas.matricula.application.usecase.ListProfessorsUseCase;
import br.edu.pucminas.matricula.application.usecase.RegisterProfessorRequest;
import br.edu.pucminas.matricula.application.usecase.RegisterProfessorResponse;
import br.edu.pucminas.matricula.application.usecase.RegisterProfessorUseCase;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/professors")
@PreAuthorize("hasRole('SECRETARIA')")
public class ProfessorAdminController {
  private final RegisterProfessorUseCase registerProfessorUseCase;
  private final ListProfessorsUseCase listProfessorsUseCase;
  private final DeleteProfessorUseCase deleteProfessorUseCase;

  public ProfessorAdminController(
      RegisterProfessorUseCase registerProfessorUseCase,
      ListProfessorsUseCase listProfessorsUseCase,
      DeleteProfessorUseCase deleteProfessorUseCase) {
    this.registerProfessorUseCase = registerProfessorUseCase;
    this.listProfessorsUseCase = listProfessorsUseCase;
    this.deleteProfessorUseCase = deleteProfessorUseCase;
  }

  @GetMapping
  public List<ListProfessorsResponse> list() {
    return listProfessorsUseCase.execute();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public RegisterProfessorResponse create(@RequestBody RegisterProfessorRequest request) {
    return registerProfessorUseCase.execute(request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("id") Long id) {
    deleteProfessorUseCase.execute(id);
  }
}
