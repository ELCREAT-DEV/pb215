package com.pb215.erp.dto.Academico;

import java.time.LocalDateTime;

public interface MatriculaResumoResponse {

    String getId();

    String getCodigoMatricula();

    String getNome();

    String getCpf();

    String getStatus();

    String getCurso();

    LocalDateTime getDataMatricula();

    String getTelefone();

    String getEmail();
}
