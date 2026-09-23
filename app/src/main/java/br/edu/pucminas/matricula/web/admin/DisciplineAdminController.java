package br.edu.pucminas.matricula.web.admin;

import br.edu.pucminas.matricula.application.usecase.CreateDisciplineRequest;
import br.edu.pucminas.matricula.application.usecase.CreateDisciplineUseCase;
import br.edu.pucminas.matricula.application.usecase.DeleteDisciplineUseCase;
import br.edu.pucminas.matricula.application.usecase.DisciplineResponse;
import br.edu.pucminas.matricula.application.usecase.ListDisciplinesUseCase;
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
@RequestMapping("/api/admin/disciplines")
@PreAuthorize("hasRole('SECRETARIA')")
public class DisciplineAdminController {
  private final ListDisciplinesUseCase listDisciplinesUseCase;
  private final CreateDisciplineUseCase createDisciplineUseCase;
  private final DeleteDisciplineUseCase deleteDisciplineUseCase;

  public DisciplineAdminController(
      ListDisciplinesUseCase listDisciplinesUseCase,
      CreateDisciplineUseCase createDisciplineUseCase,
      DeleteDisciplineUseCase deleteDisciplineUseCase) {
    this.listDisciplinesUseCase = listDisciplinesUseCase;
    this.createDisciplineUseCase = createDisciplineUseCase;
    this.deleteDisciplineUseCase = deleteDisciplineUseCase;
  }

  @GetMapping
  public List<DisciplineResponse> list() {
    return listDisciplinesUseCase.execute();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public DisciplineResponse create(@RequestBody CreateDisciplineRequest request) {
    return createDisciplineUseCase.execute(request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("id") Long id) {
    deleteDisciplineUseCase.execute(id);
  }
}
