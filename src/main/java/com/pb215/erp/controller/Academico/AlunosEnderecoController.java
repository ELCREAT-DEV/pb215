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

import com.pb215.erp.model.Academico.AlunoEnderecoModel;
import com.pb215.erp.repository.Academico.AlunoEnderecoRepository;



@RestController
@RequestMapping("academico/alunos/{alunoId}/enderecos")
public class AlunosEnderecoController {

    @Autowired
    private AlunoEnderecoRepository repository;

    @GetMapping
    public List<AlunoEnderecoModel> listar() {
        return repository.findAll();
    }

    @PostMapping
    public AlunoEnderecoModel criar(@RequestBody AlunoEnderecoModel endereco) {
        return repository.save(endereco);
    }

    @PutMapping
    public AlunoEnderecoModel atualizar(@RequestBody AlunoEnderecoModel endereco) {
        return repository.save(endereco);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
