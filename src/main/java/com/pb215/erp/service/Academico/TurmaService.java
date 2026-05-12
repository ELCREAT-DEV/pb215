package com.pb215.erp.service.Academico;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.pb215.erp.model.Academico.CursoModel;
import com.pb215.erp.model.Academico.TurmaModel;
import com.pb215.erp.repository.Academico.CursoRepository;
import com.pb215.erp.repository.Academico.TurmaRepository;

@Service
public class TurmaService {

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private CursoRepository cursoRepository;

    public TurmaModel criarTurma(TurmaModel turma) {

        if (turma.getCurso() == null || turma.getCurso().getId() == null) {
            throw new RuntimeException("Curso é obrigatório");
        }

        CursoModel curso = cursoRepository.findById(turma.getCurso().getId())
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        turma.setCurso(curso);

        int tentativas = 0;

        while (tentativas < 5) {
            try {
                String codigo = gerarCodigoTurma(curso);
                turma.setCodigoTurma(codigo);

                return turmaRepository.save(turma);

            } catch (DataIntegrityViolationException e) {
                tentativas++;
            }
        }

        throw new RuntimeException("Erro ao gerar código único de turma");
    }

    private String gerarCodigoTurma(CursoModel curso) {

        String sigla = curso.getCodigoCurso();
        String ano = String.valueOf(curso.getCreatedAt().getYear());

        String prefixo = "TUR" + sigla + ano;

        TurmaModel ultimaTurma = turmaRepository
        .findTopByCodigoTurmaStartingWithOrderByCodigoTurmaDesc(prefixo);

        int proximoNumero = 1;

        if (ultimaTurma != null) {
            String ultimoCodigo = ultimaTurma.getCodigoTurma();
            String numeroStr = ultimoCodigo.substring(prefixo.length());
            proximoNumero = Integer.parseInt(numeroStr) + 1;
        }
        String sequencial = String.format("%04d", proximoNumero);

        return prefixo + sequencial;
    }
        public TurmaModel atualizarTurma(UUID id, TurmaModel turmaReq) {

        TurmaModel turma = turmaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));

        if (turmaReq.getNome() != null) {
            turma.setNome(turmaReq.getNome());
        }

        if (turmaReq.getCapacidade() != null) {
            turma.setCapacidade(turmaReq.getCapacidade());
        }

        if (turmaReq.getTurno() != null) {
            turma.setTurno(turmaReq.getTurno());
        }

        if (turmaReq.getStatus() != null) {
            turma.setStatus(turmaReq.getStatus());
        }
        
        return turmaRepository.save(turma);
    }
}
