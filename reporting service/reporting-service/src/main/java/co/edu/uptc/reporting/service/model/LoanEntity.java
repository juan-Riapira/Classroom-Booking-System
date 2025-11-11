package co.edu.uptc.reporting.service.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.*;

@Entity
@Data
public class LoanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nameResponsible;       // Nombre completo
    private String userType;   // STUDENT o TEACHER
    private String academicProgram; // Programa académico 
    private Long classroomCode;  // Código del aula reservada
    private LocalDate loanDate;    // Fecha del préstamo
    private LocalTime startTime;   // Hora inicio
    private LocalTime endTime;     // Hora fin
    private String purpose;        // Motivo o descripción del préstamo
    private String status = "RESERVED"; // Estado: ACTIVE, RESERVED, CANCELLED
}