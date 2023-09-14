package com.facebook.exception;

import com.facebook.controller.SignupResponse;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

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
