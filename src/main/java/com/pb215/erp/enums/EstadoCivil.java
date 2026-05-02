package com.pb215.erp.enums;

public enum EstadoCivil {
    SOLTEIRO("Solteiro"),
    CASADO("Casado"),
    DIVORCIADO("Divorciado"),
    VIUVO("Viuvo"),
    UNIAO_ESTAVEL("União Estável"),
    OUTRO("Outro");

    private String descricao;
    EstadoCivil(String descricao) {
        this.descricao = descricao;
    }
    public String getDescricao() {
        return descricao;
    }

}
