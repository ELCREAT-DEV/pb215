CREATE TABLE formulario_matricula (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    token VARCHAR(255) NOT NULL UNIQUE,
    curso_id UUID NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    expira_em TIMESTAMP,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,

    CONSTRAINT fk_curso_formulario_matricula
        FOREIGN KEY (curso_id)
        REFERENCES curso(id)
        ON DELETE CASCADE
);
