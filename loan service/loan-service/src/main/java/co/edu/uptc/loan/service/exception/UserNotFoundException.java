package co.edu.uptc.loan.service.exception;

/**
 * Excepción lanzada cuando no se encuentra un usuario específico
 */
public class UserNotFoundException extends RuntimeException {
    
    public UserNotFoundException(String code) {
        super("Usuario no encontrado con código: " + code);
    }
    
    public UserNotFoundException(Long id) {
        super("Usuario no encontrado con ID: " + id);
    }
    
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}