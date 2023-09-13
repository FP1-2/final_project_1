package com.facebook.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Повертає клієнту JSON з не валідними полями, якщо такі є.
    // Обробляє контролер реєстрації /auth/signup інструкцією @Valid.
    // Змінити параметри валідації можна у DTO AppUserRequest
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse handleValidationException(MethodArgumentNotValidException ex) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError -> error.addViolation(fieldError.getField(),
                        fieldError.getDefaultMessage())
        );
        return error;
    }

    //
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDatabaseSaveException(DataIntegrityViolationException ex) {
        //Стек-трейс помилки при збереженні.
        //log.error("Handling database save error", ex);
        Map<String, Object> body = new LinkedHashMap<>();

        body.put("timestamp", LocalDateTime.now());

        String errorMessage = ex.getMessage();

        body.put("message", errorMessage.contains("Duplicate entry")
                ? errorMessage
                //Обрізаємо все зайве залишаємо "Duplicate entry <'введений рядок'>".
                .replaceAll(".*Duplicate entry '(.*?)' for key.*",
                        "Duplicate entry '$1'")
                //Інші помилки які не стосуються унікальних полів.
                : "Error occurred while saving the record.");

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}