package br.edu.pucminas.matricula.web;

import br.edu.pucminas.matricula.domain.exception.ValidationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
  @ExceptionHandler(ValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiError handleValidation(ValidationException e) {
    return new ApiError(e.getMessage());
  }

  @ExceptionHandler(BadCredentialsException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ApiError handleBadCredentials(BadCredentialsException e) {
    return new ApiError("Credenciais inválidas.");
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiError handleIntegrity(DataIntegrityViolationException e) {
    return new ApiError("Operação não permitida para o estado atual dos dados.");
  }
}
