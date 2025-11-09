package co.edu.uptc.loan.service.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.*;

@Entity
@Table(name = "loans")
@Data
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_code", nullable = false)
    private String userCode;       // Código del usuario que solicita
    
    @Column(name = "classroom_code", nullable = false)
    private Long classroomCode;  // Código del aula reservada
    
    @Column(name = "loan_date", nullable = false)
    private LocalDate loanDate;    // Fecha del préstamo
    
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;   // Hora inicio
    
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;     // Hora fin
    
    @Column(name = "purpose", length = 500)
    private String purpose;        // Motivo o descripción del préstamo
    
    @Column(name = "status", nullable = false)
    private String status = "RESERVED"; // Estado: ACTIVE, RESERVED, CANCELLED
}