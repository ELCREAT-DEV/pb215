CREATE TABLE aluno_eclesiastica (
    id SERIAL PRIMARY KEY,

    igreja_congregacao VARCHAR(100) NOT NULL,
    pastor VARCHAR(100),
    membro_desde DATE,
    ministerio_pertencente BOOLEAN,
    ministerio_qual VARCHAR(150),
    motivo_curso VARCHAR(500),
    pode_participar_sabado BOOLEAN,
    motivo_participacao VARCHAR(500),

    aluno_id UUID NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,

    CONSTRAINT fk_aluno_eclesiastica_aluno
        FOREIGN KEY (aluno_id)
        REFERENCES aluno(id)
        ON DELETE CASCADE
);
