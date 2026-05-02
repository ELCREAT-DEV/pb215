package com.pb215.erp.controller.Academico;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pb215.erp.model.Academico.AlunoContatoModel;
import com.pb215.erp.repository.Academico.AlunoContatoRepository;

@RestController
@RequestMapping("/alunos/{alunoId}/contatos")
public class AlunosContatoController {

    @Autowired
    private AlunoContatoRepository repository;

    @PostMapping
    public AlunoContatoModel criar(@RequestBody AlunoContatoModel contato) {
        return repository.save(contato);
    }

    @GetMapping
    public List<AlunoContatoModel> listar() {
        return repository.findAll();
    }

    @PutMapping
    public AlunoContatoModel atualizar(@RequestBody AlunoContatoModel contato) {
        return repository.save(contato);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        repository.deleteById(id);
    }
}