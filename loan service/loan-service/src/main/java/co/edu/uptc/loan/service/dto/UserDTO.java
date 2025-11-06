package co.edu.uptc.loan.service.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String code;       // Código institucional del usuario
    private String name;       // Nombre completo
    private String userType;   // STUDENT o TEACHER
    private Boolean active;    // Estado activo o inactivo (opcional, default true)
}