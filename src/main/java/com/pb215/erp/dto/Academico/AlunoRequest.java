package com.pb215.erp.dto.Academico;

import java.util.List;
import java.util.UUID;

import com.pb215.erp.model.Academico.AlunoContatoModel;
import com.pb215.erp.model.Academico.AlunoEclesiasticaModel;
import com.pb215.erp.model.Academico.AlunoEnderecoModel;
import com.pb215.erp.model.Academico.AlunoModel;


public class AlunoRequest {
    private AlunoModel aluno;
    private List<AlunoContatoModel> contatos;
    private List<AlunoEnderecoModel> enderecos;
    private List<AlunoEclesiasticaModel> eclesiasticas;
    private UUID turmaId;
    private UUID cursoId;
    public AlunoModel getAluno() {
        return aluno;
    }

    public List<AlunoContatoModel> getContatos() {
        return contatos;
    }

    public List<AlunoEnderecoModel> getEnderecos() {
        return enderecos;
    }

    public List<AlunoEclesiasticaModel> getEclesiasticas() {
        return eclesiasticas;
    }

    public UUID getTurmaId() {
        return turmaId;
    }

    public UUID getCursoId() {
        return cursoId;
    }
    
}
