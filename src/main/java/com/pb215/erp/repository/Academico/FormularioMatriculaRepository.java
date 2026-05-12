package com.pb215.erp.repository.Academico;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pb215.erp.model.Academico.CursoModel;
import com.pb215.erp.model.Academico.FormularioMatriculaModel;

public interface FormularioMatriculaRepository extends JpaRepository<FormularioMatriculaModel, UUID> {

    Optional<FormularioMatriculaModel> findByToken(
        String token
    );

    boolean existsByCursoAndAtivoTrue(CursoModel curso);

    List<FormularioMatriculaModel> findByCurso(CursoModel curso);
}
