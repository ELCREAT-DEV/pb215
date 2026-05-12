package com.pb215.erp.service.Academico.academico_facade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pb215.erp.dto.Academico.AlunoRequest;
import com.pb215.erp.model.Academico.AlunoModel;
import com.pb215.erp.service.Academico.AlunoService;
import com.pb215.erp.service.Academico.MatriculaService;

@Service
public class AlunoMatriculaFacade {

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private MatriculaService matriculaService;

    @Transactional
    public AlunoModel criarAlunoComMatricula(AlunoRequest request) {

        AlunoModel aluno = alunoService.criarAluno(request);

        if (request.getCursoId() != null) {
            matriculaService.matricular(aluno.getId(), request.getTurmaId(), request.getCursoId());
        }

        return aluno;
    }
}