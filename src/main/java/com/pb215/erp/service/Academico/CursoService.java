package com.pb215.erp.service.Academico;

import java.text.Normalizer;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pb215.erp.model.Academico.CursoModel;
import com.pb215.erp.repository.Academico.CursoRepository;


@Service
public class CursoService {
        @Autowired
        private CursoRepository cursoRepository;

        private String gerarCodigoCurso(String nome) {
        String sigla = nome.substring(0, 3).toUpperCase();

        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String siglaSemAcentos = pattern.matcher(Normalizer.normalize(sigla, Normalizer.Form.NFD)).replaceAll("");

        return siglaSemAcentos;
    }
    public CursoModel criarCurso(CursoModel curso) {
    curso.setCodigoCurso(gerarCodigoCurso(curso.getNome()));
    return cursoRepository.save(curso);
}
}
