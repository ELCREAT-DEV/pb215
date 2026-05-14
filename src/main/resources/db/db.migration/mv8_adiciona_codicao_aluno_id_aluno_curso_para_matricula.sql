-- =========================================================
-- MIGRATION
-- GARANTE QUE UM ALUNO
-- SÓ POSSA SE MATRICULAR UMA VEZ POR CURSO
-- =========================================================

ALTER TABLE matricula
ADD CONSTRAINT unique_aluno_curso
UNIQUE (aluno_id, curso_id);