CREATE TABLE enrollment (
  id BIGSERIAL PRIMARY KEY,
  offering_id BIGINT NOT NULL REFERENCES offering(id) ON DELETE RESTRICT,
  student_id BIGINT NOT NULL REFERENCES student(id) ON DELETE RESTRICT,
  status VARCHAR(20) NOT NULL DEFAULT 'ENROLLED',
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  cancelled_at TIMESTAMPTZ NULL
);

CREATE UNIQUE INDEX enrollment_unique_per_offering_student ON enrollment(offering_id, student_id);
CREATE INDEX enrollment_by_offering_status ON enrollment(offering_id, status);

ALTER TABLE enrollment
  ADD CONSTRAINT enrollment_status_check CHECK (status IN ('ENROLLED', 'CANCELLED'));
