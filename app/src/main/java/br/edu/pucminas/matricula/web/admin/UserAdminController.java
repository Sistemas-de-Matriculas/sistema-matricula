package br.edu.pucminas.matricula.web.admin;

import br.edu.pucminas.matricula.application.usecase.CreateUserRequest;
import br.edu.pucminas.matricula.application.usecase.CreateUserResponse;
import br.edu.pucminas.matricula.application.usecase.CreateUserUseCase;
import br.edu.pucminas.matricula.application.usecase.DeleteUserUseCase;
import br.edu.pucminas.matricula.application.usecase.ListUsersResponse;
import br.edu.pucminas.matricula.application.usecase.ListUsersUseCase;
import br.edu.pucminas.matricula.domain.exception.ValidationException;
import br.edu.pucminas.matricula.web.security.AppUserPrincipal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
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
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('SECRETARIA')")
public class UserAdminController {
  private final CreateUserUseCase createUserUseCase;
  private final ListUsersUseCase listUsersUseCase;
  private final DeleteUserUseCase deleteUserUseCase;

  public UserAdminController(
      CreateUserUseCase createUserUseCase,
      ListUsersUseCase listUsersUseCase,
      DeleteUserUseCase deleteUserUseCase) {
    this.createUserUseCase = createUserUseCase;
    this.listUsersUseCase = listUsersUseCase;
    this.deleteUserUseCase = deleteUserUseCase;
  }

  @GetMapping
  public List<ListUsersResponse> list() {
    return listUsersUseCase.execute();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CreateUserResponse create(@RequestBody CreateUserRequest request) {
    return createUserUseCase.execute(request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("id") Long id, Authentication authentication) {
    AppUserPrincipal principal = (AppUserPrincipal) authentication.getPrincipal();
    if (principal.id().equals(id)) {
      throw new ValidationException("Não é permitido remover o próprio usuário logado.");
    }
    deleteUserUseCase.execute(id);
  }
}
