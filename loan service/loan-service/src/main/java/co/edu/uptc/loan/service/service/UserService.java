package co.edu.uptc.loan.service.service;

import co.edu.uptc.loan.service.dto.UserDTO;
import co.edu.uptc.loan.service.model.User;
import co.edu.uptc.loan.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Crear usuario
    public User createUser(UserDTO userDTO) {
        // Verificar si ya existe el código
        if (userRepository.existsByCode(userDTO.getCode())) {
            throw new RuntimeException("Ya existe un usuario con el código: " + userDTO.getCode());
        }

        User user = new User();
        user.setCode(userDTO.getCode());
        user.setName(userDTO.getName());
        user.setUserType(userDTO.getUserType());
        user.setActive(userDTO.getActive() != null ? userDTO.getActive() : true);

        return userRepository.save(user);
    }

    // Obtener todos los usuarios
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Obtener usuarios activos
    public List<User> getActiveUsers() {
        return userRepository.findByActiveTrue();
    }

    // Buscar usuario por código
    public Optional<User> getUserByCode(String code) {
        return userRepository.findByCode(code);
    }

    // Validar si usuario existe y está activo
    public boolean isUserActiveByCode(String code) {
        Optional<User> user = userRepository.findByCode(code);
        return user.isPresent() && user.get().getActive();
    }

    // Obtener usuarios por tipo
    public List<User> getUsersByType(String userType) {
        return userRepository.findByUserType(userType);
    }
}