package com.pb215.erp.repository.Academico;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pb215.erp.dto.Academico.MatriculaResumoResponse;
import com.pb215.erp.model.Academico.CursoModel;
import com.pb215.erp.model.Academico.FormularioMatriculaModel;
import com.pb215.erp.model.Academico.MatriculaModel;
import com.pb215.erp.model.Academico.TurmaModel;

public interface MatriculaRepository extends JpaRepository<MatriculaModel, UUID> {

    long countByTurma(TurmaModel turma);

    boolean existsByAlunoIdAndTurmaId(UUID alunoId, UUID turmaId);

    Optional<MatriculaModel> findByAlunoIdAndTurmaId(UUID alunoId, UUID turmaId);
    
    long countByCurso(CursoModel curso);

    long countByCursoAndStatus(CursoModel curso, String status);

    long countByTurmaAndStatus(TurmaModel turma, String status);

    @Query(value = """
            SELECT
                m.id AS "id",
                m.codigo_matricula AS "codigoMatricula",
                a.nome AS nome,
                a.cpf AS cpf,
                m.status AS status,
                c.nome AS curso,
                m.created_at AS "dataMatricula",
                contato.telefone AS telefone,
                contato.email AS email
            FROM matricula m
            INNER JOIN aluno a
                ON a.id = m.aluno_id
            INNER JOIN curso c
                ON c.id = m.curso_id
            LEFT JOIN LATERAL (
                SELECT ac.telefone, ac.email
                FROM aluno_contato ac
                WHERE ac.aluno_id = a.id
                  AND ac.deleted_at IS NULL
                ORDER BY ac.id
                LIMIT 1
            ) contato ON TRUE
            WHERE m.deleted_at IS NULL
            ORDER BY m.created_at DESC
            """, nativeQuery = true)
    List<MatriculaResumoResponse> listarResumoParaFront();   
        long countByFormularioAndStatus(
        FormularioMatriculaModel formulario,
        String status
);
    
}
