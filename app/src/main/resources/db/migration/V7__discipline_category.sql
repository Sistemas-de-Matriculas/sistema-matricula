ALTER TABLE discipline
  ADD COLUMN category VARCHAR(20) NOT NULL DEFAULT 'MANDATORY';

ALTER TABLE discipline
  ADD CONSTRAINT discipline_category_check CHECK (category IN ('MANDATORY', 'OPTIONAL'));
