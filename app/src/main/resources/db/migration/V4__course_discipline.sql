CREATE TABLE course (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(120) NOT NULL UNIQUE,
  credits INT NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE discipline (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(120) NOT NULL,
  course_id BIGINT NOT NULL REFERENCES course(id) ON DELETE RESTRICT,
  professor_id BIGINT NOT NULL REFERENCES professor(id) ON DELETE RESTRICT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX discipline_unique_per_course ON discipline(course_id, name);
