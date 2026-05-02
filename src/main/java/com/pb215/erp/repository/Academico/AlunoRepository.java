package com.pb215.erp.repository.Academico;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pb215.erp.model.Academico.AlunoModel;

public interface AlunoRepository extends JpaRepository<AlunoModel, UUID> {
    List<AlunoModel> findByDeletedAtIsNull();

}