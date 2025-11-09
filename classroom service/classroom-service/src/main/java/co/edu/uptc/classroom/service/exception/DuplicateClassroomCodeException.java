package co.edu.uptc.classroom.service.exception;

public class DuplicateClassroomCodeException extends RuntimeException {
    
    public DuplicateClassroomCodeException(String code) {
        super("Classroom code already exists: " + code);
    }
}