package br.edu.pucminas.matricula.web.admin;

import br.edu.pucminas.matricula.application.usecase.DeleteOfferingUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/offerings")
@PreAuthorize("hasRole('SECRETARIA')")
public class OfferingAdminController {
  private final DeleteOfferingUseCase deleteOfferingUseCase;

  public OfferingAdminController(DeleteOfferingUseCase deleteOfferingUseCase) {
    this.deleteOfferingUseCase = deleteOfferingUseCase;
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("id") Long id) {
    deleteOfferingUseCase.execute(id);
  }
}

