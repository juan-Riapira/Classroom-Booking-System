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
    
    // Campos calculados (para respuestas)
    private Integer startHour;     // Hora de inicio (número)
    private Integer duration;      // Duración en minutos
    private Integer weekNumber;    // Número de semana
    private Integer monthNumber;   // Número de mes
}