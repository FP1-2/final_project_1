package com.facebook.exception;

import com.facebook.controller.auth.SignupResponse;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ValidationErrorResponse implements SignupResponse {
    private List<Violation> violations = new ArrayList<>();

    public void addViolation(String fieldName, String message) {
        violations.add(new Violation(fieldName, message));
    }

    @Data
    public static class Violation {
        private final String fieldName;
        private final String message;
    }
}
