package com.pb215.erp.repository.Academico;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pb215.erp.model.Academico.MatriculaModel;
import com.pb215.erp.model.Academico.TurmaModel;

public interface MatriculaRepository extends JpaRepository<MatriculaModel, UUID> {

    long countByTurma(TurmaModel turma);

    boolean existsByAlunoIdAndTurmaId(UUID alunoId, UUID turmaId);

    Optional<MatriculaModel> findByAlunoIdAndTurmaId(UUID alunoId, UUID turmaId);
    
    
}
