-- Permite que o aluno se matricule novamente após cancelar:
-- a unicidade passa a valer apenas para matrículas ativas.
DROP INDEX enrollment_unique_per_offering_student;

CREATE UNIQUE INDEX enrollment_unique_active_per_offering_student
  ON enrollment(offering_id, student_id)
  WHERE status = 'ENROLLED';
