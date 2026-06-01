package com.pb215.erp.controller.Academico;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import com.pb215.erp.dto.Academico.MatriculaRequest;
import com.pb215.erp.dto.Academico.MatriculaResumoResponse;
import com.pb215.erp.model.Academico.MatriculaModel;
import com.pb215.erp.service.Academico.MatriculaService;

@RestController
@RequestMapping("/academico/matricula")
public class MatriculaController {

    @Autowired
    private MatriculaService matriculaService;

    @PostMapping("/matricular")
    public MatriculaModel matricular(
            @RequestParam UUID alunoId,
            @RequestParam UUID turmaId,
            @RequestParam UUID cursoId) {

        return matriculaService.matricular(alunoId, turmaId, cursoId);
    }

    @GetMapping("/list")
    public List <MatriculaModel> listar() {
        return matriculaService.listarMatriculas();
    }

    @GetMapping("/list/resume")
    public List<MatriculaResumoResponse> listarResumoParaFront() {
        return matriculaService.listarResumoParaFront();
    }

    @GetMapping("/{id}")
    public MatriculaModel getMatriculaById(@PathVariable UUID id) {
        return matriculaService.getMatriculaById(id);
    }

    @PutMapping("update/{id}")
    public MatriculaModel atualizar(@PathVariable UUID id, @Valid @RequestBody MatriculaRequest request) {
        return matriculaService.atualizarMatricula(id, request);
    }

    @DeleteMapping("delete/{id}")
    public void deletar(@PathVariable UUID id) {
        matriculaService.deletarMatricula(id);
    }


}
