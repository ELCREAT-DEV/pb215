package com.pb215.erp.service.Academico;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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

        turma.setCodigoTurma(gerarCodigoTurma(curso));

        return turmaRepository.save(turma);
    }

    private String gerarCodigoTurma(CursoModel curso) {

        // pega sigla do curso (ex: ADS)
        String sigla = curso.getCodigoCurso();

        // pega ano do curso (createdAt)
        String ano = String.valueOf(curso.getCreatedAt().getYear());

        String prefixo = sigla + ano ;

        // busca quantas turmas já existem com esse padrão
        int count = turmaRepository.countByCodigoTurmaStartingWith(prefixo);

        String sequencial = String.format("%04d", count + 1);

        return "TUR" + prefixo + sequencial;
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

        // campos protegidos (importante manter assim)
        // codigoTurma NÃO atualiza
        // curso NÃO atualiza aqui

        return turmaRepository.save(turma);
    }
}
