package com.pb215.erp.repository.Academico;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pb215.erp.model.Academico.AlunoEnderecoModel;

public interface AlunoEnderecoRepository extends JpaRepository<AlunoEnderecoModel, Long> {

    List<AlunoEnderecoModel> findByAlunoId(UUID alunoId);

    List<AlunoEnderecoModel> findByDeletedAtIsNull();

    List<AlunoEnderecoModel> findByAlunoIdAndDeletedAtIsNull(UUID alunoId);
}