package co.edu.uptc.loan.service.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true, nullable = false)
    private String code;       // Código institucional del usuario
    
    @Column(name = "name", nullable = false)
    private String name;       // Nombre completo
    
    @Column(name = "user_type", nullable = false)
    private String userType;   // STUDENT o TEACHER
    
    @Column(name = "academic_program")
    private String academicProgram; // Programa académico 
    
    @Column(name = "active")
    private Boolean active = true; // Estado activo o inactivo
}