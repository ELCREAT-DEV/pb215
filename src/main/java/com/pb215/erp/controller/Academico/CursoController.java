package com.pb215.erp.controller.Academico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pb215.erp.model.Academico.CursoModel;
import com.pb215.erp.repository.Academico.CursoRepository;
import com.pb215.erp.service.Academico.CursoService;


@RestController
@RequestMapping("/academico/curso")
public class CursoController {
    
    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private CursoService cursoService;

    @PostMapping("/create")
    public CursoModel criar(@RequestBody CursoModel curso) {
        return cursoService.criarCurso(curso);
    }

    @GetMapping("/list")
    public Iterable<CursoModel> listar() {
        return cursoRepository.findAll();
    }
    @PutMapping("/update")
    public CursoModel atualizar(CursoModel curso) {
        return cursoRepository.save(curso);
    }

    @DeleteMapping("/delete")
    public void deletar(CursoModel curso) {
        cursoRepository.delete(curso);
    }
}
