package co.edu.uptc.loan.service.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;       // Código institucional del usuario
    private String name;       // Nombre completo
    private String userType;   // STUDENT o TEACHER
    private Boolean active = true; // Estado activo o inactivo
}