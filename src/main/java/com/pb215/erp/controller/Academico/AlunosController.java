package com.pb215.erp.controller.Academico;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pb215.erp.dto.Academico.AlunoRequest;
import com.pb215.erp.model.Academico.AlunoModel;
import com.pb215.erp.service.Academico.AlunoService;

import com.pb215.erp.service.Academico.academico_facade.AlunoMatriculaFacade;

@RestController
@RequestMapping("academico/aluno")
public class AlunosController {

    @Autowired
    private AlunoService service;

    @Autowired
    private AlunoMatriculaFacade facade;

    @PostMapping("/create")
    public AlunoModel criar(@RequestBody AlunoRequest request) {
        return facade.criarAlunoComMatricula(request);
    }

    @GetMapping("list")
    public List<AlunoModel> listar() {
        return service.listarAlunos();
    }

    @GetMapping("{id}")
    public AlunoModel getAlunoById(@PathVariable UUID id) {
        return service.getAlunoById(id);
    }

    @PutMapping("update/{id}")
    public AlunoModel atualizar(@PathVariable UUID id, @RequestBody AlunoRequest request) {
        return service.atualizarAluno(id, request);
    }

    @DeleteMapping("delete/{id}")
    public void deletar(@PathVariable UUID id) {
        service.deletarAluno(id);
    }
    
}
