package com.pb215.erp.enums.Academico;

public enum StatusEscolar {

    ENSINO_MEDIO_INCOMPLETO("Ensino Médio Incompleto"),
    ENSINO_MEDIO_COMPLETO("Ensino Médio Completo"),
    SUPERIOR_INCOMPLETO("Superior Incompleto"),
    SUPERIOR_COMPLETO("Superior Completo"),
    POS_GRADUACAO_INCOMPLETA("Pós-graduação Incompleta"),
    POS_GRADUACAO("Pós-graduação"),
    MESTRADO_INCOMPLETO("Mestrado Incompleto"),
    MESTRADO_COMPLETO("Mestrado Completo"),
    DOUTORADO_INCOMPLETO("Doutorado Incompleto"),
    DOUTORADO_COMPLETO("Doutorado Completo");
    private String descricao;

    StatusEscolar(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}