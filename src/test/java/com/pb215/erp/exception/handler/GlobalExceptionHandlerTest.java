package com.pb215.erp.exception.handler;

import com.pb215.erp.dto.ViolationDetail;
import com.pb215.erp.exception.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @RestController
    static class TestController {

        record SimpleRequest(@NotBlank String nome) {}

        @GetMapping("/test/not-found")
        void notFound() { throw new ResourceNotFoundException("Aluno não encontrado"); }

        @GetMapping("/test/business")
        void business() { throw new BusinessException("Turma está fechada"); }

        @GetMapping("/test/conflict")
        void conflict() { throw new ConflictException("Já existe um formulário ativo para este curso"); }

        @GetMapping("/test/validation")
        void validation() {
            throw new ValidationException(List.of(new ViolationDetail("cpf", "CPF é obrigatório")));
        }

        @GetMapping("/test/unexpected")
        void unexpected() { throw new RuntimeException("erro inesperado"); }

        @PostMapping("/test/valid")
        void valid(@Valid @RequestBody SimpleRequest request) {}
    }

    @BeforeEach
    void setup() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders
                .standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    void resourceNotFound_returns404WithProblemDetail() throws Exception {
        mockMvc.perform(get("/test/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("Aluno não encontrado"))
                .andExpect(jsonPath("$.title").value("Não Encontrado"))
                .andExpect(jsonPath("$.instance").value("/test/not-found"));
    }

    @Test
    void businessException_returns422() throws Exception {
        mockMvc.perform(get("/test/business"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.detail").value("Turma está fechada"))
                .andExpect(jsonPath("$.instance").value("/test/business"));
    }

    @Test
    void conflictException_returns409() throws Exception {
        mockMvc.perform(get("/test/conflict"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.detail").value("Já existe um formulário ativo para este curso"));
    }

    @Test
    void validationException_returns400WithViolations() throws Exception {
        mockMvc.perform(get("/test/validation"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.violations[0].campo").value("cpf"))
                .andExpect(jsonPath("$.violations[0].detalhe").value("CPF é obrigatório"));
    }

    @Test
    void methodArgumentNotValid_returns400WithFieldViolations() throws Exception {
        mockMvc.perform(post("/test/valid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": \"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.violations").isArray())
                .andExpect(jsonPath("$.violations[0].campo").value("nome"))
                .andExpect(jsonPath("$.violations[0].detalhe").isNotEmpty())
                .andExpect(jsonPath("$.instance").value("/test/valid"));
    }

    @Test
    void malformedJson_returns400() throws Exception {
        mockMvc.perform(post("/test/valid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{broken json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("Requisição Inválida"));
    }

    @Test
    void unexpectedException_returns500WithInstance() throws Exception {
        mockMvc.perform(get("/test/unexpected"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.title").value("Erro Interno"))
                .andExpect(jsonPath("$.instance").value("/test/unexpected"));
    }

    @Test
    void wrongHttpMethod_returns405NotSwallowedAs500() throws Exception {
        // /test/business é GET-only; POST deve disparar HttpRequestMethodNotSupportedException
        // que estende ErrorResponseException. Verifica se o parent trata (405) ou se
        // handleGeneric engole como 500.
        mockMvc.perform(post("/test/business"))
                .andExpect(status().isMethodNotAllowed());
    }
}
