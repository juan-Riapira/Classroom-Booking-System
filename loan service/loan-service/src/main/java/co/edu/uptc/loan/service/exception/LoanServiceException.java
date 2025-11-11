package co.edu.uptc.loan.service.exception;

/**
 * Excepción personalizada para errores específicos del loan-service
 * Proporciona mensajes claros para validaciones de negocio
 */
public class LoanServiceException extends RuntimeException {
    
    private final String errorCode;
    
    public LoanServiceException(String message) {
        super(message);
        this.errorCode = "LOAN_ERROR";
    }
    
    public LoanServiceException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public LoanServiceException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "LOAN_ERROR";
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    
    public static class TimeConflictException extends LoanServiceException {
        public TimeConflictException(String classroomCode) {
            super("Ya existe un préstamo en ese horario para el aula '" + classroomCode + "'", "TIME_CONFLICT");
        }
    }
    
    public static class LoanNotFoundException extends LoanServiceException {
        public LoanNotFoundException(Long id) {
            super("Préstamo no encontrado con ID: " + id, "LOAN_NOT_FOUND");
        }
    }
    
    public static class InvalidStatusException extends LoanServiceException {
        public InvalidStatusException(String status) {
            super("Estado inválido '" + status + "'. Estados válidos: ACTIVE, RESERVED, CANCELLED", "INVALID_STATUS");
        }
    }
    
    public static class ClassroomNotAvailableException extends LoanServiceException {
        public ClassroomNotAvailableException(String classroomCode) {
            super("El aula '" + classroomCode + "' no está disponible en el horario solicitado", "CLASSROOM_NOT_AVAILABLE");
        }
    }
}