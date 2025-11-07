package co.edu.uptc.loan.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para loan-service
 * Proporciona respuestas JSON consistentes para todas las excepciones
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LoanServiceException.UserNotActiveException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotActive(LoanServiceException.UserNotActiveException ex) {
        return buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            ex.getErrorCode(),
            ex.getMessage(),
            "El usuario especificado no existe o no está activo para crear préstamos"
        );
    }

    @ExceptionHandler(LoanServiceException.TimeConflictException.class)
    public ResponseEntity<Map<String, Object>> handleTimeConflict(LoanServiceException.TimeConflictException ex) {
        return buildErrorResponse(
            HttpStatus.CONFLICT,
            ex.getErrorCode(),
            ex.getMessage(),
            "Ya existe un préstamo reservado en ese horario para el aula especificada"
        );
    }

    @ExceptionHandler(LoanServiceException.LoanNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleLoanNotFound(LoanServiceException.LoanNotFoundException ex) {
        return buildErrorResponse(
            HttpStatus.NOT_FOUND,
            ex.getErrorCode(),
            ex.getMessage(),
            "El préstamo solicitado no existe en el sistema"
        );
    }

    @ExceptionHandler(LoanServiceException.InvalidStatusException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidStatus(LoanServiceException.InvalidStatusException ex) {
        return buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            ex.getErrorCode(),
            ex.getMessage(),
            "El estado proporcionado no es válido. Use: ACTIVE, RESERVED, CANCELLED"
        );
    }

    @ExceptionHandler(LoanServiceException.class)
    public ResponseEntity<Map<String, Object>> handleLoanServiceException(LoanServiceException ex) {
        return buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            ex.getErrorCode(),
            ex.getMessage(),
            "Error en el servicio de préstamos"
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            "INVALID_ARGUMENT",
            ex.getMessage(),
            "Los datos proporcionados no son válidos"
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        return buildErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "INTERNAL_ERROR",
            "Error interno del servidor",
            "Ha ocurrido un error inesperado. Contacte al administrador"
        );
    }

    /**
     * Construye una respuesta de error consistente
     */
    private ResponseEntity<Map<String, Object>> buildErrorResponse(
            HttpStatus status, String errorCode, String message, String detail) {
        
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        errorResponse.put("status", status.value());
        errorResponse.put("error", status.getReasonPhrase());
        errorResponse.put("errorCode", errorCode);
        errorResponse.put("message", message);
        errorResponse.put("detail", detail);
        errorResponse.put("service", "loan-service");
        
        return new ResponseEntity<>(errorResponse, status);
    }
}