package com.pb215.erp.controller.Academico;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pb215.erp.dto.Academico.FormularioMatriculaRequest;
import com.pb215.erp.model.Academico.FormularioMatriculaModel;
import com.pb215.erp.model.Academico.MatriculaModel;
import com.pb215.erp.service.Academico.FormularioMatriculaService;

@RestController
@RequestMapping("/academico/formulario")
public class FormularioMatriculaController {

    @Autowired
    private FormularioMatriculaService formularioService;

    @PostMapping("/gerar")
    public FormularioMatriculaModel gerarFormulario(@RequestBody FormularioMatriculaRequest request) {
        return formularioService.gerarFormulario(request.getCursoId(), request.getExpiraEm(), request.getVagas());
    }

    @PostMapping("/matricular")
    public MatriculaModel matricularComFormulario(
            @RequestParam String token,
            @RequestParam java.util.UUID alunoId) {
        return formularioService.matricularComFormulario(token, alunoId);
    }

    @GetMapping("/list")
    public List<FormularioMatriculaModel> listar() {
        return formularioService.listarFormularios();
    }

    @GetMapping("/{id}")
    public FormularioMatriculaModel buscarPorId(@PathVariable java.util.UUID id) {
        return formularioService.buscarPorId(id);
    }

    @GetMapping("/token/{token}")
    public ResponseEntity<FormularioMatriculaModel> buscarPorToken(
            @PathVariable String token
    ) {
        return ResponseEntity.ok(
                formularioService.buscarPorToken(token)
        );
    }

    @PutMapping("/{id}")
    public FormularioMatriculaModel atualizar(@PathVariable java.util.UUID id, @RequestBody FormularioMatriculaRequest request) {
        return formularioService.atualizarFormulario(id, request);  
    }
    
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable java.util.UUID id) {
        formularioService.deletarFormulario(id);    
    }
}
