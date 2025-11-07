package co.edu.uptc.loan.service.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class LoanDTO {
    private Long id;               // ID del préstamo (para respuestas)
    private String userCode;       // Código del usuario que solicita
    private String classroomCode;  // Código del aula reservada
    private LocalDate loanDate;    // Fecha del préstamo
    private LocalTime startTime;   // Hora inicio
    private LocalTime endTime;     // Hora fin
    private String purpose;        // Motivo o descripción del préstamo
    private String status;         // Estado: ACTIVE, RESERVED, CANCELLED
    
    // ✅ SOLO DATOS BASE - SIN CÁLCULOS (DIRECTRIZ DEL DIRECTOR)
    // Los cálculos (startHour, duration, weekNumber, monthNumber) 
    // son responsabilidad del REPORTING-SERVICE, no del loan-service
}