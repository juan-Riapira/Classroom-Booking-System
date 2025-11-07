package co.edu.uptc.loan.service.controller;

import co.edu.uptc.loan.service.dto.UserDTO;
import co.edu.uptc.loan.service.model.User;
import co.edu.uptc.loan.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    // POST /api/users - Crear usuario
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO newUser = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    // GET /api/users - Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // GET /api/users/active - Obtener usuarios activos
    @GetMapping("/active")
    public ResponseEntity<List<UserDTO>> getActiveUsers() {
        List<UserDTO> activeUsers = userService.getActiveUsers();
        return ResponseEntity.ok(activeUsers);
    }

    // GET /api/users/{code} - Buscar usuario por código
    @GetMapping("/{code}")
    public ResponseEntity<UserDTO> getUserByCode(@PathVariable String code) {
        Optional<UserDTO> user = userService.getUserByCode(code);
        return user.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/users/type/{userType} - Obtener usuarios por tipo
    @GetMapping("/type/{userType}")
    public ResponseEntity<List<UserDTO>> getUsersByType(@PathVariable String userType) {
        List<UserDTO> users = userService.getUsersByType(userType);
        return ResponseEntity.ok(users);
    }

    // GET /api/users/{code}/validate - Validar si usuario está activo
    @GetMapping("/{code}/validate")
    public ResponseEntity<String> validateUser(@PathVariable String code) {
        boolean isActive = userService.isUserActiveByCode(code);
        if (isActive) {
            return ResponseEntity.ok("Usuario " + code + " está activo y válido");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}