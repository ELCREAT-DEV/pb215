package com.pb215.erp.repository.Academico;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pb215.erp.model.Academico.TurmaModel;

public interface TurmaRepository extends JpaRepository<TurmaModel, UUID> {

    // buscar 
    List<TurmaModel> findByTurmaFechadaFalse();

    List<TurmaModel> findByCursoId(UUID cursoId);

    boolean existsByCodigoTurma(String codigoTurma);

    TurmaModel  findTopByCodigoTurmaStartingWithOrderByCodigoTurmaDesc(String prefixo);

}