package com.facebook.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String DUPLICATE_ENTRY = "Duplicate entry '";

    /**
     * Оброблює винятки валідації для запитів,
     * де дані проходять валідацію за допомогою @Valid.
     * Якщо з'являються невалідні поля, повертає клієнту JSON
     * з інформацією про ці поля.
     *
     * @param ex - виняток, що містить інформацію
     *             про помилки валідації.
     * @return ValidationErrorResponse - об'єкт,
     *         який містить інформацію про невалідні поля.
     */
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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDatabaseSaveException(DataIntegrityViolationException ex) {
        Map<String, Object> body = new LinkedHashMap<>();

        body.put("timestamp", LocalDateTime.now());

        String errorMessage = ex.getMessage();

        if (errorMessage.contains("Duplicate entry")) {
            int start = errorMessage.indexOf(DUPLICATE_ENTRY) + DUPLICATE_ENTRY.length();
            int end = errorMessage.indexOf("'", start);
            if (end != -1) {
                errorMessage = DUPLICATE_ENTRY + errorMessage.substring(start, end) + "'";
            } else {
                errorMessage = "Error occurred while interpreting the duplicate entry message.";
            }
        } else {
            errorMessage = "Error occurred while saving the record.";
        }

        body.put("message", errorMessage);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>>
    handleAlreadyExistsException(AlreadyExistsException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("type", "Conflict Error");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<Map<String, Object>>
    handleRegistrationException(RegistrationException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("type", "Registration Error");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>>
    handleRuntimeException(RuntimeException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        log.warn(ex.getMessage());
        body.put("type", "Internal Server Error");
        body.put("message", "An internal error occurred. Please try again later.");

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Оброблює винятки типу {@link UnauthorizedException}.
     *
     * <p>Перехоплює та формує відповідь для клієнта
     * з повідомленням та HTTP статусом 401 (Unauthorized).</p>
     *
     * @param ex екземпляр винятка.
     * @return відповідь для клієнта із мапою даних,
     * що містить тип винятка та повідомлення.
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, Object>>
    handleUnauthorizedAccessException(UnauthorizedException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("type", "Unauthorized Access Error");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>>
    handleNotFoundException(NotFoundException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("type", "Not Found Error");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex){
        log.warn(ex.getMessage());
        return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<String> handleInvalidTokenException(InvalidTokenException ex){
        log.warn(ex.getMessage());
        return new ResponseEntity<>("Invalid or expired reset token", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailSendingException.class)
    public ResponseEntity<String> handleMailException(EmailSendingException ex) {
        log.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to send email: " + ex.getMessage());
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException ex){
        log.warn(ex.getMessage());
        return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.NOT_FOUND);
    }

}