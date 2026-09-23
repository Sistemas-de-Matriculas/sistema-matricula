package br.edu.pucminas.matricula.web.auth;

import br.edu.pucminas.matricula.web.security.AppUserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthenticationManager authenticationManager;
  private final HttpSessionSecurityContextRepository securityContextRepository =
      new HttpSessionSecurityContextRepository();

  public AuthController(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @PostMapping("/login")
  public MeDto login(
      @RequestBody LoginDto request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
    if (request == null || request.username() == null || request.password() == null) {
      throw new BadCredentialsException("Credenciais inválidas.");
    }

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.username().trim(), request.password()));

    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    securityContextRepository.saveContext(context, httpRequest, httpResponse);
    SecurityContextHolder.setContext(context);

    AppUserPrincipal principal = (AppUserPrincipal) authentication.getPrincipal();
    return new MeDto(principal.id(), principal.getUsername(), principal.role());
  }

  @GetMapping("/me")
  public MeDto me(Authentication authentication) {
    AppUserPrincipal principal = (AppUserPrincipal) authentication.getPrincipal();
    return new MeDto(principal.id(), principal.getUsername(), principal.role());
  }

  @PostMapping("/logout")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void logout(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }
    SecurityContextHolder.clearContext();
  }
}
