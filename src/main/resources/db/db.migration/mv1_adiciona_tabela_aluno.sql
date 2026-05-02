CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE TABLE aluno (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    imagem_url VARCHAR(255),

    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    rg VARCHAR(20),

    data_nascimento DATE,
    sexo VARCHAR(10), ,
    estado_civil VARCHAR(20),
    status_escolar VARCHAR(20),
    profissao VARCHAR(100),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);