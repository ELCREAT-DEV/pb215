package com.pb215.erp.service.Academico;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pb215.erp.dto.Academico.MatriculaRequest;
import com.pb215.erp.model.Academico.AlunoModel;
import com.pb215.erp.model.Academico.MatriculaModel;
import com.pb215.erp.model.Academico.TurmaModel;
import com.pb215.erp.repository.Academico.AlunoRepository;
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

public MatriculaModel matricular(UUID alunoId, UUID turmaId) {

    AlunoModel aluno = alunoRepository.findById(alunoId)
            .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

    TurmaModel turma = turmaRepository.findById(turmaId)
            .orElseThrow(() -> new RuntimeException("Turma não encontrada"));

    return matriculaRepository.findByAlunoIdAndTurmaId(alunoId, turmaId)
            .orElseGet(() -> {

                if (Boolean.TRUE.equals(turma.getTurmaFechada())) {
                    throw new RuntimeException("Turma está fechada");
                }

                long ocupacao = matriculaRepository.countByTurma(turma);

                if (ocupacao >= turma.getCapacidade()) {
                    throw new RuntimeException("Turma sem vagas");
                }

                MatriculaModel matricula = new MatriculaModel();
                matricula.setAluno(aluno);
                matricula.setTurma(turma);
                matricula.setCodigoMatricula(gerarCodigoMatricula(turma));
                matricula.setStatus("PENDENTE");

                return matriculaRepository.save(matricula);
            });
}
    
    private String gerarCodigoMatricula(TurmaModel turma) {

        String codigoCurso = turma.getCurso().getCodigoCurso(); 
        // ex: ADS

        String ano = String.valueOf(java.time.Year.now().getValue()); 
        // ex: 2026

        long count = matriculaRepository.countByTurma(turma);

        String sequencial = String.format("%04d", count + 1);

        String uuid = UUID.randomUUID().toString().replace("-", "");
        String ultimos3 = uuid.substring(uuid.length() - 3).toUpperCase();

        return codigoCurso + ano + sequencial + ultimos3;
    }

    public List<MatriculaModel> listarMatriculas() {
        return matriculaRepository.findAll();
    }

    public MatriculaModel atualizarMatricula(UUID id, MatriculaRequest request) {
        MatriculaModel matricula = matriculaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada"));

        if (request.getStatus() != null) {
            matricula.setStatus(request.getStatus());
        }

        return matriculaRepository.save(matricula);
    }

    public MatriculaModel getMatriculaById(UUID id) {
        return matriculaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada"));
    }


    public void deletarMatricula(UUID id) {
        if (!matriculaRepository.existsById(id)) {
            throw new RuntimeException("Matrícula não encontrada");
        }
        matriculaRepository.deleteById(id);
    }
}
