package com.pb215.erp.repository.Academico;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pb215.erp.model.Academico.CursoModel;

public interface CursoRepository extends JpaRepository<CursoModel, UUID> {
    List<CursoModel> findByDeletedAtIsNull();
    List<CursoModel> findByIdAndDeletedAtIsNull(UUID id);
    List<CursoModel> findByCodigoCursoStartingWith(String prefix);
}
