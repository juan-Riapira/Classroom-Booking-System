package co.edu.uptc.loan.service.service;

import co.edu.uptc.loan.service.dto.UserDTO;
import co.edu.uptc.loan.service.model.User;
import co.edu.uptc.loan.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Crear usuario
    public UserDTO createUser(UserDTO userDTO) {
        // Verificar si ya existe el código
        if (userRepository.existsByCode(userDTO.getCode())) {
            throw new RuntimeException("Ya existe un usuario con el código: " + userDTO.getCode());
        }

        User user = new User();
        user.setCode(userDTO.getCode());
        user.setName(userDTO.getName());
        user.setUserType(userDTO.getUserType());
        user.setAcademicProgram(userDTO.getAcademicProgram());
        user.setActive(userDTO.getActive() != null ? userDTO.getActive() : true);

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    // Obtener todos los usuarios
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Obtener usuarios activos
    public List<UserDTO> getActiveUsers() {
        return userRepository.findByActiveTrue()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Buscar usuario por código
    public Optional<UserDTO> getUserByCode(String code) {
        return userRepository.findByCode(code)
                .map(this::convertToDTO);
    }

    // Validar si usuario existe y está activo
    public boolean isUserActiveByCode(String code) {
        Optional<User> user = userRepository.findByCode(code);
        return user.isPresent() && user.get().getActive();
    }

    // Obtener usuarios por tipo
    public List<UserDTO> getUsersByType(String userType) {
        return userRepository.findByUserType(userType)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Obtener usuarios por programa académico
    public List<UserDTO> getUsersByAcademicProgram(String academicProgram) {
        return userRepository.findByAcademicProgram(academicProgram)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // === MÉTODOS DE CONVERSIÓN ENTITY ↔ DTO ===
    
    /**
     * Convierte Entity a DTO para respuestas API
     */
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setCode(user.getCode());
        dto.setName(user.getName());
        dto.setUserType(user.getUserType());
        dto.setAcademicProgram(user.getAcademicProgram());
        dto.setActive(user.getActive());
        return dto;
    }
}