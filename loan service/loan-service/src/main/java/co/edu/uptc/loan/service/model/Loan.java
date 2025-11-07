package co.edu.uptc.loan.service.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.*;

@Entity
@Data
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userCode;       // Código del usuario que solicita
    private String classroomCode;  // Código del aula reservada
    private LocalDate loanDate;    // Fecha del préstamo
    private LocalTime startTime;   // Hora inicio
    private LocalTime endTime;     // Hora fin
    private String purpose;        // Motivo o descripción del préstamo
    private String status = "RESERVED"; // Estado: ACTIVE, RESERVED, CANCELLED
}