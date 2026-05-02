package com.pb215.erp.repository.Academico;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pb215.erp.model.Academico.AlunoContatoModel;

public interface AlunoContatoRepository extends JpaRepository<AlunoContatoModel, Long> {

    List <AlunoContatoModel> findByAlunoId(UUID alunoId);

    List <AlunoContatoModel> findByDeletedAtIsNull();

    List <AlunoContatoModel> findByAlunoIdAndDeletedAtIsNull(UUID alunoId);
}