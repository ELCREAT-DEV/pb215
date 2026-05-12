package com.pb215.erp.controller.Academico;


import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pb215.erp.model.Academico.TurmaModel;
import com.pb215.erp.repository.Academico.TurmaRepository;
import com.pb215.erp.service.Academico.TurmaService;


@RestController
@RequestMapping("/academico/turma")
public class TurmaController {
    
    @Autowired
    private TurmaRepository turmaRepository;
    @Autowired
    private TurmaService turmaService;

    @PostMapping
    public TurmaModel criar(@RequestBody TurmaModel turma) {
        return turmaService.criarTurma(turma);
    }
    
    @GetMapping("/list")
    public Iterable<TurmaModel> listar() {
        return turmaRepository.findAll();
    }

    @GetMapping("/{id}")
    public TurmaModel buscarPorId(@PathVariable UUID id) {
        return turmaRepository.findById(id).orElseThrow();
    }
    
    @PutMapping("/{id}")
    public TurmaModel atualizar(@PathVariable UUID id, @RequestBody TurmaModel turmaReq) {
        return turmaService.atualizarTurma(id, turmaReq);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable UUID id) {
        turmaRepository.deleteById(id);
    }
}