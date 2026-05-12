package com.pb215.erp.service.Academico;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pb215.erp.dto.Academico.MatriculaRequest;
import com.pb215.erp.dto.Academico.MatriculaResumoResponse;
import com.pb215.erp.model.Academico.AlunoModel;
import com.pb215.erp.model.Academico.CursoModel;
import com.pb215.erp.model.Academico.MatriculaModel;
import com.pb215.erp.model.Academico.TurmaModel;
import com.pb215.erp.repository.Academico.AlunoRepository;
import com.pb215.erp.repository.Academico.CursoRepository;
import com.pb215.erp.repository.Academico.FormularioMatriculaRepository;
import com.pb215.erp.repository.Academico.MatriculaRepository;
import com.pb215.erp.repository.Academico.TurmaRepository;

@Service
public class MatriculaService {
    
    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private FormularioMatriculaRepository formularioRepository;

    private static final String STATUS_ATIVO = "ATIVO";

public MatriculaModel matricular(UUID alunoId, UUID turmaId, UUID cursoId) {

    AlunoModel aluno = alunoRepository.findById(alunoId)
            .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

    CursoModel curso = cursoRepository.findById(cursoId)
            .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

    TurmaModel turma = null;

    if (turmaId != null) {
        turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));

        if (curso.getId() == null || turma.getCurso() == null || !turma.getCurso().getId().equals(curso.getId())) {
            throw new RuntimeException("Turma não pertence ao curso informado");
        }

        if (Boolean.TRUE.equals(turma.getTurmaFechada())) {
            throw new RuntimeException("Turma está fechada");
        }

        long ocupacao = matriculaRepository.countByTurma(turma);

        if (ocupacao >= turma.getCapacidade()) {
            throw new RuntimeException("Turma sem vagas");
        }
    }

    MatriculaModel matricula = new MatriculaModel();
    matricula.setAluno(aluno);
    matricula.setCurso(curso);
    matricula.setTurma(turma);
    matricula.setCodigoMatricula(gerarCodigoMatricula(curso));
    matricula.setStatus("PENDENTE");

    MatriculaModel matriculaSalva = matriculaRepository.save(matricula);
    atualizarVagasFormulariosDoCurso(curso);

    return matriculaSalva;
}
    
    

    public String gerarCodigoMatricula(CursoModel curso) {

        String codigoCurso = curso.getCodigoCurso(); // ex: ADS

        String ano = String.valueOf(java.time.Year.now().getValue()); // 2026

        long count = matriculaRepository.countByCurso(curso);

        String sequencial = String.format("%04d", count + 1);

        String uuid = UUID.randomUUID().toString().replace("-", "");
        String ultimos3 = uuid.substring(uuid.length() - 3).toUpperCase();

        return codigoCurso + ano + sequencial + ultimos3;
    }

    public List<MatriculaModel> listarMatriculas() {
        return matriculaRepository.findAll();
    }

    public List<MatriculaResumoResponse> listarResumoParaFront() {
        return matriculaRepository.listarResumoParaFront();
    }

    public MatriculaModel atualizarMatricula(UUID id, MatriculaRequest request) {
        MatriculaModel matricula = matriculaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada"));

        if (request.getStatus() != null) {
            if (!STATUS_ATIVO.equals(matricula.getStatus()) && STATUS_ATIVO.equals(request.getStatus())) {
                validarVagasFormulario(matricula);
            }
            matricula.setStatus(request.getStatus());
        }

        MatriculaModel matriculaSalva = matriculaRepository.save(matricula);
        atualizarVagasFormulariosDoCurso(matriculaSalva.getCurso());

        return matriculaSalva;
    }

    public MatriculaModel getMatriculaById(UUID id) {
        return matriculaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada"));
    }


    public void deletarMatricula(UUID id) {
        MatriculaModel matricula = matriculaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada"));
        CursoModel curso = matricula.getCurso();

        matriculaRepository.deleteById(id);
        atualizarVagasFormulariosDoCurso(curso);
    }

    public MatriculaModel alocarEmTurma(UUID matriculaId, UUID turmaId) {

    MatriculaModel matricula = matriculaRepository.findById(matriculaId)
            .orElseThrow(() -> new RuntimeException("Matrícula não encontrada"));

    TurmaModel turma = turmaRepository.findById(turmaId)
            .orElseThrow(() -> new RuntimeException("Turma não encontrada"));

    long ocupacao = matriculaRepository.countByTurma(turma);

    if (ocupacao >= turma.getCapacidade()) {
        throw new RuntimeException("Turma sem vagas");
    }

    matricula.setTurma(turma);

    return matriculaRepository.save(matricula);
}

private void validarVagasFormulario(MatriculaModel matricula) {
    CursoModel curso = matricula.getCurso();
    if (curso == null) {
        return;
    }

    long matriculasAtivas = matriculaRepository.countByCursoAndStatus(curso, STATUS_ATIVO);
    formularioRepository.findByCurso(curso).stream()
            .filter(formulario -> Boolean.TRUE.equals(formulario.getAtivo()))
            .filter(formulario -> formulario.getVagas() != null && formulario.getVagas() > 0)
            .filter(formulario -> matriculasAtivas >= formulario.getVagas())
            .findFirst()
            .ifPresent(formulario -> {
                throw new RuntimeException("Vagas do formulário atingidas");
            });
}

private void atualizarVagasFormulariosDoCurso(CursoModel curso) {
    if (curso == null) {
        return;
    }

    long matriculasAtivas = matriculaRepository.countByCursoAndStatus(curso, STATUS_ATIVO);

    formularioRepository.findByCurso(curso).forEach(formulario -> {
        int vagas = formulario.getVagas() == null ? 0 : formulario.getVagas();
        formulario.setVagasContagem(Math.max(vagas - Math.toIntExact(matriculasAtivas), 0));
        if (formulario.getVagasContagem() <= 0 && vagas > 0) {
            formulario.setAtivo(Boolean.FALSE);
        }
        formularioRepository.save(formulario);
    });
}
}
