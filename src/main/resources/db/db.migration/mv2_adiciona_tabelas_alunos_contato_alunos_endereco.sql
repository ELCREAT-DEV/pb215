CREATE table aluno_contato (
    id long primary key generated always as identity,
    aluno_id uuid not null,
    telefone varchar(20),
    telefone_emergencia varchar(20),
    email varchar(100),
    created_at timestamp default current_timestamp,
    updated_at timestamp,
    deleted_at timestamp,
    foreign key (aluno_id) references aluno(id)
)

CREATE table aluno_endereco (
    id long primary key generated always as identity,
    aluno_id uuid not null,
    logradouro varchar(255),
    numero varchar(20),
    complemento varchar(255),
    bairro varchar(100),
    cidade varchar(100),
    estado varchar(2),
    cep varchar(10),
    created_at timestamp default current_timestamp,
    updated_at timestamp,
    deleted_at timestamp,
    foreign key (aluno_id) references aluno(id)
);