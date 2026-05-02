package com.pb215.erp.enums.Academico;

public enum StatusEscolar {

    ENSINO_MEDIO_COMPLETO("Ensino Médio Completo"),
    SUPERIOR_INCOMPLETO("Superior Incompleto"),
    SUPERIOR_COMPLETO("Superior Completo"),
    POS_GRADUACAO("Pós-graduação");

    private String descricao;

    StatusEscolar(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}