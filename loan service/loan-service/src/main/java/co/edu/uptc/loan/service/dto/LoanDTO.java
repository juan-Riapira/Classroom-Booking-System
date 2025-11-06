package co.edu.uptc.loan.service.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class LoanDTO {
    private String userCode;       // Código del usuario que solicita
    private String classroomCode;  // Código del aula reservada
    private LocalDate loanDate;    // Fecha del préstamo
    private LocalTime startTime;   // Hora inicio
    private LocalTime endTime;     // Hora fin
    private String purpose;        // Motivo o descripción del préstamo
    private String status;         // Estado: ACTIVE, RESERVED, CANCELLED (opcional)
}