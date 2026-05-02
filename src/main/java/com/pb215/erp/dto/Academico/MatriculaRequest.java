package com.pb215.erp.dto.Academico;

import java.util.UUID;

public class MatriculaRequest {

    private UUID alunoId;
    private UUID turmaId;
    private String status;

    public UUID getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(UUID alunoId) {
        this.alunoId = alunoId;
    }

    public UUID getTurmaId() {
        return turmaId;
    }

    public void setTurmaId(UUID turmaId) {
        this.turmaId = turmaId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}