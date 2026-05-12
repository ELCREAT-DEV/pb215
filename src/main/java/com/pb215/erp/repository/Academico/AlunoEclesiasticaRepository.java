package com.pb215.erp.repository.Academico;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pb215.erp.model.Academico.AlunoEclesiasticaModel;

public interface AlunoEclesiasticaRepository extends JpaRepository<AlunoEclesiasticaModel, Long> {

    List<AlunoEclesiasticaModel> findByAlunoId(UUID alunoId);

    List<AlunoEclesiasticaModel> findByDeletedAtIsNull();

    List<AlunoEclesiasticaModel> findByAlunoIdAndDeletedAtIsNull(UUID alunoId);
}
