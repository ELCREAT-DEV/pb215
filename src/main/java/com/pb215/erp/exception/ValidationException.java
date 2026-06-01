package com.pb215.erp.exception;

import com.pb215.erp.dto.ViolationDetail;
import org.springframework.http.HttpStatus;

import java.util.List;

public class ValidationException extends AppException {

    private final List<ViolationDetail> violations;

    public ValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
        this.violations = List.of();
    }

    public ValidationException(List<ViolationDetail> violations) {
        super("Um ou mais campos estão inválidos", HttpStatus.BAD_REQUEST);
        this.violations = List.copyOf(violations);
    }

    public List<ViolationDetail> getViolations() {
        return violations;
    }
}
