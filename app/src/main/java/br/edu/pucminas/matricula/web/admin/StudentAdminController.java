package br.edu.pucminas.matricula.web.admin;

import br.edu.pucminas.matricula.application.usecase.DeleteStudentUseCase;
import br.edu.pucminas.matricula.application.usecase.ListStudentsResponse;
import br.edu.pucminas.matricula.application.usecase.ListStudentsUseCase;
import br.edu.pucminas.matricula.application.usecase.RegisterStudentRequest;
import br.edu.pucminas.matricula.application.usecase.RegisterStudentResponse;
import br.edu.pucminas.matricula.application.usecase.RegisterStudentUseCase;
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
@RequestMapping("/api/admin/students")
@PreAuthorize("hasRole('SECRETARIA')")
public class StudentAdminController {
  private final RegisterStudentUseCase registerStudentUseCase;
  private final ListStudentsUseCase listStudentsUseCase;
  private final DeleteStudentUseCase deleteStudentUseCase;

  public StudentAdminController(
      RegisterStudentUseCase registerStudentUseCase,
      ListStudentsUseCase listStudentsUseCase,
      DeleteStudentUseCase deleteStudentUseCase) {
    this.registerStudentUseCase = registerStudentUseCase;
    this.listStudentsUseCase = listStudentsUseCase;
    this.deleteStudentUseCase = deleteStudentUseCase;
  }

  @GetMapping
  public List<ListStudentsResponse> list() {
    return listStudentsUseCase.execute();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public RegisterStudentResponse create(@RequestBody RegisterStudentRequest request) {
    return registerStudentUseCase.execute(request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("id") Long id) {
    deleteStudentUseCase.execute(id);
  }
}
