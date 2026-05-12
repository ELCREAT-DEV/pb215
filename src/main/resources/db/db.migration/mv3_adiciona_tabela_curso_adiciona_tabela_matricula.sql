CREATE TABLE curso (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    codigo_curso VARCHAR(5) NOT NULL UNIQUE,
    eclesiastico BOOLEAN NOT NULL DEFAULT FALSE,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE turma (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    nome VARCHAR(100) NOT NULL,
    codigo_turma VARCHAR(10) NOT NULL UNIQUE,

    curso_id UUID,

    data_inicio DATE,
    data_fim DATE,

    turno VARCHAR(20),
    capacidade INTEGER,

    turma_fechada BOOLEAN DEFAULT FALSE,

    status VARCHAR(20),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,

    CONSTRAINT fk_curso FOREIGN KEY (curso_id) REFERENCES curso(id)
);
CREATE TABLE matricula (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    codigo_matricula VARCHAR(25) NOT NULL UNIQUE,

    aluno_id UUID NOT NULL,
    curso_id UUID NOT NULL,
    turma_id UUID,
    formulario_id UUID,

    data_matricula TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    status VARCHAR(20) DEFAULT 'PENDENTE',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,

    CONSTRAINT fk_aluno
        FOREIGN KEY (aluno_id)
        REFERENCES aluno(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_curso
        FOREIGN KEY (curso_id)
        REFERENCES curso(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_turma
        FOREIGN KEY (turma_id)
        REFERENCES turma(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_formulario
        FOREIGN KEY (formulario_id)
        REFERENCES formulario_matricula(id)
        ON DELETE SET NULL,

    CONSTRAINT unique_matricula
        UNIQUE (aluno_id, turma_id)
);