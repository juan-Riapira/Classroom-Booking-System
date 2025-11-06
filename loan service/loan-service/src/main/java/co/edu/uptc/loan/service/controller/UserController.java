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
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        try {
            User newUser = userService.createUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }

    // GET /api/users - Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // GET /api/users/active - Obtener usuarios activos
    @GetMapping("/active")
    public ResponseEntity<List<User>> getActiveUsers() {
        List<User> activeUsers = userService.getActiveUsers();
        return ResponseEntity.ok(activeUsers);
    }

    // GET /api/users/{code} - Buscar usuario por código
    @GetMapping("/{code}")
    public ResponseEntity<?> getUserByCode(@PathVariable String code) {
        Optional<User> user = userService.getUserByCode(code);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario con código " + code + " no encontrado");
        }
    }

    // GET /api/users/type/{userType} - Obtener usuarios por tipo
    @GetMapping("/type/{userType}")
    public ResponseEntity<List<User>> getUsersByType(@PathVariable String userType) {
        List<User> users = userService.getUsersByType(userType);
        return ResponseEntity.ok(users);
    }

    // GET /api/users/{code}/validate - Validar si usuario está activo
    @GetMapping("/{code}/validate")
    public ResponseEntity<?> validateUser(@PathVariable String code) {
        boolean isActive = userService.isUserActiveByCode(code);
        if (isActive) {
            return ResponseEntity.ok("Usuario " + code + " está activo y válido");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario " + code + " no existe o no está activo");
        }
    }
}