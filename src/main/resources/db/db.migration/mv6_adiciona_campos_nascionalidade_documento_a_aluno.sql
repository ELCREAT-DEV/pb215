ALTER TABLE aluno
ADD COLUMN nacionalidade VARCHAR(100),
ADD COLUMN documento VARCHAR(100);
IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='aluno' AND column_name='nacionalidade') THEN
    UPDATE aluno SET nacionalidade = 'Sem nacionalidade' WHERE nacionalidade IS NULL;
END IF;
IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='aluno' AND column_name='documento') THEN
    UPDATE aluno SET documento = '0000000000' WHERE documento IS NULL;
END IF;