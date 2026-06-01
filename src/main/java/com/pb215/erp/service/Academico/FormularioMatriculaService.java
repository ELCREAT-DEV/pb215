package com.pb215.erp.service.Academico;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pb215.erp.dto.Academico.FormularioMatriculaRequest;
import com.pb215.erp.exception.BusinessException;
import com.pb215.erp.exception.ConflictException;
import com.pb215.erp.exception.ResourceNotFoundException;
import com.pb215.erp.exception.ValidationException;
import com.pb215.erp.model.Academico.CursoModel;
import com.pb215.erp.model.Academico.FormularioMatriculaModel;
import com.pb215.erp.model.Academico.MatriculaModel;
import com.pb215.erp.repository.Academico.CursoRepository;
import com.pb215.erp.repository.Academico.FormularioMatriculaRepository;
import com.pb215.erp.repository.Academico.MatriculaRepository;

@Service
public class FormularioMatriculaService {

    @Autowired
    private FormularioMatriculaRepository formularioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private MatriculaService matriculaService;

    private static final String STATUS_PENDENTE = "PENDENTE";

    public FormularioMatriculaModel gerarFormulario(
            UUID cursoId,
            LocalDateTime expiraEm,
            Integer vagas
    ) {

        CursoModel curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado"));

        if (!Boolean.TRUE.equals(curso.getEclesiastico())) {
            throw new BusinessException("Formulário só pode ser gerado para curso eclesiástico");
        }

        if (formularioRepository.existsByCursoAndAtivoTrue(curso)) {
            throw new ConflictException("Já existe um formulário ativo para este curso");
        }

        if (vagas == null) {
            throw new ValidationException("É necessário informar a quantidade de vagas do formulário");
        }
        if (vagas <= 0) {
            throw new ValidationException("A quantidade de vagas do formulário deve ser maior que zero");
        }

        FormularioMatriculaModel formulario =
                new FormularioMatriculaModel();

        formulario.setCurso(curso);
        formulario.setToken(UUID.randomUUID().toString());
        formulario.setExpiraEm(expiraEm);
        formulario.setAtivo(Boolean.TRUE);
        formulario.setVagas(vagas);

        // Inicialmente todas as vagas estão disponíveis
        formulario.setVagasContagem(vagas);

        return formularioRepository.save(formulario);
    }

    public MatriculaModel matricularComFormulario(
            String token,
            UUID alunoId
    ) {

        FormularioMatriculaModel formulario =
                formularioRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Formulário não encontrado"));

        if (Boolean.FALSE.equals(formulario.getAtivo())) {
            throw new BusinessException("Formulário inativo");
        }

        if (formulario.getExpiraEm() != null && LocalDateTime.now().isAfter(formulario.getExpiraEm())) {
            formulario.setAtivo(Boolean.FALSE);
            formularioRepository.save(formulario);
            throw new BusinessException("Formulário expirado");
        }

        CursoModel curso = formulario.getCurso();

        if (curso == null) {
            throw new BusinessException("Formulário não está vinculado a nenhum curso");
        }

        atualizarVagasContagem(formulario);

        if (formulario.getVagas() == null || formulario.getVagas() <= 0) {
            throw new BusinessException("Formulário sem vagas configuradas");
        }

        if (formulario.getVagasContagem() <= 0) {
            formulario.setAtivo(Boolean.FALSE);
            formularioRepository.save(formulario);
            throw new BusinessException("Vagas do formulário atingidas");
        }

        MatriculaModel matricula =
                matriculaService.matricular(
                        alunoId,
                        null,
                        curso.getId()
                );

        // Vincula a matrícula ao formulário
        matricula.setFormulario(formulario);

        matriculaRepository.save(matricula);

        atualizarVagasContagem(formulario);

        if (formulario.getVagasContagem() <= 0) {
            formulario.setAtivo(Boolean.FALSE);
        }

        formularioRepository.save(formulario);

        return matricula;
    }

    public List<FormularioMatriculaModel> listarFormularios() {

        List<FormularioMatriculaModel> formularios =
                formularioRepository.findAll();

        verificarExpiracaoEmLote(formularios);

        return formularios;
    }

    public FormularioMatriculaModel buscarPorId(UUID id) {

        FormularioMatriculaModel formulario =
                formularioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Formulário não encontrado"));

        return verificarExpiracao(formulario);
    }

    public void deletarFormulario(UUID id) {

        FormularioMatriculaModel formulario =
                formularioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Formulário não encontrado"));

        formulario.setAtivo(Boolean.FALSE);
        formulario.setDeletedAt(LocalDateTime.now());

        formularioRepository.save(formulario);
    }

    public FormularioMatriculaModel atualizarFormulario(
            UUID id,
            FormularioMatriculaRequest request
    ) {

        FormularioMatriculaModel formulario =
                formularioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Formulário não encontrado"));

        if (request.getCursoId() != null) {

            CursoModel curso =
                    cursoRepository.findById(request.getCursoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado"));

            formulario.setCurso(curso);
        }

        if (request.getAtivo() != null) {
            formulario.setAtivo(request.getAtivo());
        }

        if (request.getExpiraEm() != null) {
            formulario.setExpiraEm(request.getExpiraEm());
        }

        if (request.getVagas() != null) {

            if (request.getVagas() <= 0) {
                throw new ValidationException("A quantidade de vagas do formulário deve ser maior que zero");
            }

            formulario.setVagas(request.getVagas());
        }

        if (
                formulario.getExpiraEm() != null &&
                LocalDateTime.now().isAfter(formulario.getExpiraEm())
        ) {

            formulario.setAtivo(Boolean.FALSE);
        }

        atualizarVagasContagem(formulario);

        return formularioRepository.save(formulario);
    }

    private FormularioMatriculaModel verificarExpiracao(
            FormularioMatriculaModel formulario
    ) {

        if (
                Boolean.TRUE.equals(formulario.getAtivo()) &&
                formulario.getExpiraEm() != null &&
                LocalDateTime.now().isAfter(formulario.getExpiraEm())
        ) {

            formulario.setAtivo(Boolean.FALSE);

            formularioRepository.save(formulario);
        }

        atualizarVagasContagem(formulario);

        formularioRepository.save(formulario);

        return formulario;
    }

    private void verificarExpiracaoEmLote(
            List<FormularioMatriculaModel> formularios
    ) {

        formularios.stream()
                .filter(formulario ->
                        Boolean.TRUE.equals(formulario.getAtivo()) &&
                        formulario.getExpiraEm() != null &&
                        LocalDateTime.now().isAfter(
                                formulario.getExpiraEm()
                        )
                )
                .forEach(formulario -> {

                    formulario.setAtivo(Boolean.FALSE);

                    formularioRepository.save(formulario);
                });

        formularios.forEach(formulario -> {

            atualizarVagasContagem(formulario);

            formularioRepository.save(formulario);
        });
    }

    private void atualizarVagasContagem(
            FormularioMatriculaModel formulario
    ) {

        int vagas =
                formulario.getVagas() == null
                        ? 0
                        : formulario.getVagas();

        long matriculasAtivas = 0;

        // Conta apenas matrículas deste formulário
        matriculasAtivas =
                matriculaRepository.countByFormularioAndStatus(
                        formulario,
                        STATUS_PENDENTE
                );

        formulario.setVagasContagem(
                Math.max(
                        vagas - Math.toIntExact(matriculasAtivas),
                        0
                )
        );
    }

    public FormularioMatriculaModel buscarPorToken(
            String token
    ) {

        FormularioMatriculaModel formulario =
                formularioRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Formulário não encontrado"));

        return verificarExpiracao(formulario);
    }
}