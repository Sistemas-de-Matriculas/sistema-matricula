ALTER TABLE app_user
  ADD CONSTRAINT app_user_role_chk CHECK (role IN ('ALUNO', 'PROFESSOR', 'SECRETARIA'));
