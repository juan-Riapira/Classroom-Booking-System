package co.edu.uptc.classroom.service.exception;

public class ClassroomNotFoundException extends RuntimeException {
    
    public ClassroomNotFoundException(String message) {
        super(message);
    }
    
    public ClassroomNotFoundException(Long id) {
        super("Classroom not found with ID: " + id);
    }
}