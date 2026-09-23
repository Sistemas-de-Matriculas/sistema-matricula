CREATE TABLE semester (
  id BIGSERIAL PRIMARY KEY,
  code VARCHAR(30) NOT NULL UNIQUE,
  enrollment_open BOOLEAN NOT NULL DEFAULT FALSE,
  enrollment_start TIMESTAMPTZ NULL,
  enrollment_end TIMESTAMPTZ NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE offering (
  id BIGSERIAL PRIMARY KEY,
  semester_id BIGINT NOT NULL REFERENCES semester(id) ON DELETE RESTRICT,
  discipline_id BIGINT NOT NULL REFERENCES discipline(id) ON DELETE RESTRICT,
  capacity INT NOT NULL DEFAULT 60,
  status VARCHAR(20) NOT NULL DEFAULT 'OFFERED',
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX offering_unique_per_semester ON offering(semester_id, discipline_id);

ALTER TABLE offering
  ADD CONSTRAINT offering_status_check CHECK (status IN ('OFFERED', 'CONFIRMED', 'CANCELLED'));
