package br.edu.pucminas.matricula.application.usecase;

import br.edu.pucminas.matricula.domain.model.Enrollment;
import br.edu.pucminas.matricula.domain.model.Student;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Stub do UC11 (Notificar Sistema de Cobranças).
 * Apenas registra os dados enviados ao sistema externo (RN06).
 */
@Service
public class BillingNotificationService {
  private static final Logger log = LoggerFactory.getLogger(BillingNotificationService.class);

  public void notifyEnrollment(Student student, List<Enrollment> enrollments) {
    log.info(
        "[UC11] Notificação de cobrança enviada para aluno={} (userId={}) - {} disciplinas matriculadas",
        student.name(),
        student.userId(),
        enrollments.size());
  }
}
