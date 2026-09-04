package com.practicerest.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Errores de validación (@Valid en controllers)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, Object> error = new HashMap<>();
        Map<String, String> fieldErrors = new HashMap<>();
        
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage()));
        
        error.put("timestamp", LocalDateTime.now());
        error.put("status", 400);
        error.put("message", "Errores de validación");
        error.put("errors", fieldErrors);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // 2. Recurso no encontrado (excepción personalizada)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        Map<String, Object> error = new HashMap<>();
        
        error.put("timestamp", LocalDateTime.now());
        error.put("status", 404);
        error.put("message", ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // 3. Error de tipo (ej. pasar texto donde va número en @PathVariable)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, Object> error = new HashMap<>();
        
        error.put("timestamp", LocalDateTime.now());
        error.put("status", 400);
        error.put("message", "Error de tipo: " + ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // 4. Error de base de datos (DataAccessException)
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, Object>> handleDatabaseError(DataAccessException ex) {
        Map<String, Object> error = new HashMap<>();
        
        error.put("timestamp", LocalDateTime.now());
        error.put("status", 500);
        error.put("message", "Error de base de datos");
        error.put("details", ex.getMostSpecificCause().getMessage());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    // 5. Fallback para cualquier otra excepción no prevista
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> error = new HashMap<>();
        
        error.put("timestamp", LocalDateTime.now());
        error.put("status", 500);
        error.put("message", "Error interno del servidor");
        error.put("details", ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
