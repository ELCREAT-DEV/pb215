package com.pb215.erp.dto.Academico;

import java.util.List;
import java.util.UUID;

import com.pb215.erp.model.Academico.AlunoContatoModel;
import com.pb215.erp.model.Academico.AlunoEnderecoModel;
import com.pb215.erp.model.Academico.AlunoModel;


public class AlunoRequest {
    private AlunoModel aluno;
    private List<AlunoContatoModel> contatos;
    private List<AlunoEnderecoModel> enderecos;
    private UUID turmaId;

    public AlunoModel getAluno() {
        return aluno;
    }

    public List<AlunoContatoModel> getContatos() {
        return contatos;
    }

    public List<AlunoEnderecoModel> getEnderecos() {
        return enderecos;
    }

    public UUID getTurmaId() {
        return turmaId;
    }
    
}
