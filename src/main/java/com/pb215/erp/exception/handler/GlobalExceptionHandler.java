package com.pb215.erp.exception.handler;

import com.pb215.erp.dto.ViolationDetail;
import com.pb215.erp.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        log.warn("Not found: {}", ex.getMessage());
        return buildProblem(ex.getStatus(), "Não Encontrado", ex.getMessage(), null, req);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ProblemDetail> handleBusiness(BusinessException ex, HttpServletRequest req) {
        log.warn("Business rule violated: {}", ex.getMessage());
        return buildProblem(ex.getStatus(), "Regra de Negócio", ex.getMessage(), null, req);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ProblemDetail> handleConflict(ConflictException ex, HttpServletRequest req) {
        log.warn("Conflict: {}", ex.getMessage());
        return buildProblem(ex.getStatus(), "Conflito", ex.getMessage(), null, req);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ProblemDetail> handleValidation(ValidationException ex, HttpServletRequest req) {
        log.warn("Validation error: {}", ex.getMessage());
        return buildProblem(ex.getStatus(), "Erro de Validação", ex.getMessage(),
                ex.getViolations().isEmpty() ? null : ex.getViolations(), req);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneric(Exception ex, HttpServletRequest req) {
        String uri = req != null ? req.getRequestURI() : "unknown";
        log.error("Unexpected error on {}", uri, ex);
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor");
        problem.setTitle("Erro Interno");
        problem.setInstance(safeUri(uri));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        log.warn("Unreadable request body: {}", ex.getMostSpecificCause().getMessage());
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Corpo da requisição inválido ou ausente");
        problem.setTitle("Requisição Inválida");
        setInstance(problem, request);
        return ResponseEntity.badRequest().body(problem);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<ViolationDetail> violations = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> new ViolationDetail(err.getField(), err.getDefaultMessage()))
                .toList();

        log.warn("Validation failed for {}: {} violation(s)", ex.getBindingResult().getObjectName(), violations.size());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Um ou mais campos estão inválidos");
        problem.setTitle("Erro de Validação");
        problem.setProperty("violations", violations);
        setInstance(problem, request);

        return ResponseEntity.badRequest().body(problem);
    }

    private ResponseEntity<ProblemDetail> buildProblem(
            HttpStatus status, String title, String detail,
            List<ViolationDetail> violations, HttpServletRequest req) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        problem.setInstance(safeUri(req != null ? req.getRequestURI() : "unknown"));

        if (violations != null) {
            problem.setProperty("violations", violations);
        }

        return ResponseEntity.status(status).body(problem);
    }

    private void setInstance(ProblemDetail problem, WebRequest request) {
        if (request instanceof ServletWebRequest swr) {
            problem.setInstance(safeUri(swr.getRequest().getRequestURI()));
        }
    }

    private URI safeUri(String rawPath) {
        try {
            return URI.create(rawPath);
        } catch (IllegalArgumentException e) {
            return URI.create("about:blank");
        }
    }
}
