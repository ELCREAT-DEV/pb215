package com.pb215.erp.service.Academico;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pb215.erp.dto.Academico.AlunoRequest;
import com.pb215.erp.model.Academico.AlunoContatoModel;
import com.pb215.erp.model.Academico.AlunoEclesiasticaModel;
import com.pb215.erp.model.Academico.AlunoEnderecoModel;
import com.pb215.erp.model.Academico.AlunoModel;
import com.pb215.erp.repository.Academico.AlunoContatoRepository;
import com.pb215.erp.repository.Academico.AlunoEclesiasticaRepository;
import com.pb215.erp.repository.Academico.AlunoEnderecoRepository;
import com.pb215.erp.repository.Academico.AlunoRepository;
import jakarta.transaction.Transactional;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private AlunoContatoRepository contatoRepository;

    @Autowired
    private AlunoEnderecoRepository enderecoRepository;

    @Autowired
    private AlunoEclesiasticaRepository eclesiasticaRepository;

    @Autowired
    private MatriculaService matriculaService;

    public void validarAlunoCompleto(AlunoModel aluno) {

        if (aluno.getNome() == null || aluno.getNome().isBlank()) {
            throw new RuntimeException("Aluno sem nome");
        }

        if (aluno.getCpf() == null || aluno.getCpf().isBlank()) {
            throw new RuntimeException("Aluno sem CPF");
        }

        if (aluno.getDataNascimento() == null) {
            throw new RuntimeException("Aluno sem data de nascimento");
        }

        List<AlunoContatoModel> contatos = contatoRepository.findByAlunoId(aluno.getId());
        if (contatos == null || contatos.isEmpty()) {
            throw new RuntimeException("Aluno sem contato");
        }

        List<AlunoEnderecoModel> enderecos = enderecoRepository.findByAlunoId(aluno.getId());
        if (enderecos == null || enderecos.isEmpty()) {
            throw new RuntimeException("Aluno sem endereço");
        }
    }

    @Transactional
    public AlunoModel criarAluno(AlunoRequest request) {

        if (request.getAluno().getCpf() == null || request.getAluno().getCpf().isEmpty()) {
            throw new RuntimeException("CPF é obrigatório");
        }
        if (request.getContatos() == null || request.getContatos().isEmpty()) {
            throw new RuntimeException("É necessário preencher o contato do aluno");
        }

        if (request.getEnderecos() == null || request.getEnderecos().isEmpty()) {
            throw new RuntimeException("É necessário preencher o endereço do aluno");
        }

        AlunoModel alunoSalvo = alunoRepository.save(request.getAluno());

        request.getContatos().forEach(c -> {
            c.setAluno(alunoSalvo);
            contatoRepository.save(c);
        });

        request.getEnderecos().forEach(e -> {
            e.setAluno(alunoSalvo);
            enderecoRepository.save(e);
        });

        if (request.getEclesiasticas() != null && !request.getEclesiasticas().isEmpty()) {
            request.getEclesiasticas().forEach(e -> {
                e.setAluno(alunoSalvo);
                eclesiasticaRepository.save(e);
            });
        }
        
        return alunoSalvo;
    }

    
    public List<AlunoModel> listarAlunos() {
        return alunoRepository.findByDeletedAtIsNull();
    }
    public AlunoModel getAlunoById(UUID id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com id: " + id));
    }
    public AlunoModel atualizarAluno(UUID id, AlunoRequest request) {

        AlunoModel aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com id: " + id));

        AlunoModel req = request.getAluno();

        if (req.getNome() != null) {
            aluno.setNome(req.getNome());
        }

        if (req.getCpf() != null) {
            aluno.setCpf(req.getCpf());
        }

        if (req.getDataNascimento() != null) {
            aluno.setDataNascimento(req.getDataNascimento());
        }

        if (req.getImagemUrl() != null) {
            aluno.setImagemUrl(req.getImagemUrl());
        }

        return alunoRepository.save(aluno);
    }
    //Soft delete do aluno e dados relacionados
    @Transactional
    public void deletarAluno(UUID id) {
        AlunoModel aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com id: " + id));

        LocalDateTime now = LocalDateTime.now();

        aluno.setDeletedAt(now);
        alunoRepository.save(aluno);

        // deletar contatos
        List<AlunoContatoModel> contatos = contatoRepository.findByAlunoId(id);
        contatos.forEach(c -> c.setDeletedAt(now));
        contatoRepository.saveAll(contatos);

        // deletar endereços
        List<AlunoEnderecoModel> enderecos = enderecoRepository.findByAlunoId(id);
        enderecos.forEach(e -> e.setDeletedAt(now));
        enderecoRepository.saveAll(enderecos);

        // deletar dados eclesiásticos
        List<AlunoEclesiasticaModel> eclesiasticas = eclesiasticaRepository.findByAlunoId(id);
        eclesiasticas.forEach(e -> e.setDeletedAt(now));
        eclesiasticaRepository.saveAll(eclesiasticas);
    }
}
