package br.edu.pucminas.matricula.application;

import br.edu.pucminas.matricula.application.usecase.BootstrapAdminUser;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class BootstrapAdminRunner implements ApplicationRunner {
  private final BootstrapAdminUser bootstrapAdminUser;

  public BootstrapAdminRunner(BootstrapAdminUser bootstrapAdminUser) {
    this.bootstrapAdminUser = bootstrapAdminUser;
  }

  @Override
  public void run(ApplicationArguments args) {
    bootstrapAdminUser.executeIfEmpty();
  }
}
