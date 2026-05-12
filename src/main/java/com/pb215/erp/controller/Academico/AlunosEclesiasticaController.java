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

import com.pb215.erp.model.Academico.AlunoEclesiasticaModel;
import com.pb215.erp.repository.Academico.AlunoEclesiasticaRepository;

@RestController
@RequestMapping("/academico/alunos/{alunoId}/eclesiasticas")
public class AlunosEclesiasticaController {

    @Autowired
    private AlunoEclesiasticaRepository repository;

    @PostMapping
    public AlunoEclesiasticaModel criar(@RequestBody AlunoEclesiasticaModel eclesiastica) {
        return repository.save(eclesiastica);
    }

    @GetMapping
    public List<AlunoEclesiasticaModel> listar() {
        return repository.findAll();
    }

    @PutMapping
    public AlunoEclesiasticaModel atualizar(@RequestBody AlunoEclesiasticaModel eclesiastica) {
        return repository.save(eclesiastica);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
