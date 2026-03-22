package com.invex.employees.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.invex.employees.utils.Constants.*;

/**
 * Global exception handler for API
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles bean validation errors for @Valid request bodies.
     *
     * @param ex the exception
     * @return formatted validation error response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> response = new LinkedHashMap<>();
        response.put(TIMESTAMP, LocalDateTime.now());
        response.put(STATUS, HttpStatus.BAD_REQUEST.value());
        response.put(MESSAGE, "Validation failed");
        response.put(ERRORS, errors);
        return ResponseEntity.badRequest().body(response);
    }


    /**
     * Handles data integrity violations from JPA
     *
     * @param ex the exception
     * @return formatted error response
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.error("Data integrity violation", ex);

        Map<String, Object> response = buildErrorResponse(
                HttpStatus.CONFLICT,
                "Database constraint violation"
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * Handles delete operations when the entity does not exist.
     *
     * @param ex the exception
     * @return formatted error response
     */
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<Map<String, Object>> handleEmptyResult(EmptyResultDataAccessException ex) {
        log.warn("Entity not found for delete operation: {}", ex.getMessage());

        Map<String, Object> response = buildErrorResponse(
                HttpStatus.NOT_FOUND,
                "Resource not found"
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Handles runtime exceptions
     *
     * @param ex the exception
     * @return formatted error response
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        log.warn("Runtime exception: {}", ex.getMessage());

        HttpStatus status = ex.getMessage() != null && ex.getMessage().toLowerCase().contains("not found")
                ? HttpStatus.NOT_FOUND
                : HttpStatus.BAD_REQUEST;

        Map<String, Object> response = buildErrorResponse(status, ex.getMessage());

        return ResponseEntity.status(status).body(response);
    }

    /**
     * Handles unexpected errors.
     *
     * @param ex the exception
     * @return formatted error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        log.error("Unexpected error", ex);

        Map<String, Object> response = buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error"
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Builds a standard error response body.
     *
     * @param status  HTTP status
     * @param message error message
     * @return response body
     */
    private Map<String, Object> buildErrorResponse(HttpStatus status, String message) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put(TIMESTAMP, LocalDateTime.now());
        response.put(STATUS, status.value());
        response.put(MESSAGE, message);
        return response;
    }
}
