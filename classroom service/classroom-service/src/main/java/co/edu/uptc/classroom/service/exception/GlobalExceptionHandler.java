package co.edu.uptc.classroom.service.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejador global de excepciones para el classroom-service
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClassroomNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleClassroomNotFound(ClassroomNotFoundException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.NOT_FOUND.value());
        error.put("error", "Classroom Not Found");
        error.put("message", ex.getMessage());
        error.put("path", "/api/classrooms");
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(DuplicateClassroomCodeException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateCode(DuplicateClassroomCodeException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.CONFLICT.value());
        error.put("error", "Duplicate Classroom Code");
        error.put("message", ex.getMessage());
        error.put("path", "/api/classrooms");
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.CONFLICT.value());
        error.put("error", "Data Integrity Violation");
        
        // Detectar si es un problema de código duplicado
        String message = ex.getMessage().toLowerCase();
        if (message.contains("code") && message.contains("duplicate")) {
            error.put("message", "Classroom code already exists. Please use a different code.");
        } else if (message.contains("unique") || message.contains("duplicate")) {
            error.put("message", "Duplicate value detected. Please check your input.");
        } else {
            error.put("message", "Data integrity constraint violation");
        }
        
        error.put("path", "/api/classrooms");
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("error", "Validation Failed");
        
        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        
        error.put("validationErrors", validationErrors);
        error.put("message", "Invalid input data");
        error.put("path", "/api/classrooms");
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.put("error", "Internal Server Error");
        error.put("message", "An unexpected error occurred");
        error.put("path", "/api/classrooms");
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}