package co.edu.uptc.loan.service.controller;

import co.edu.uptc.loan.service.dto.UserDTO;
import co.edu.uptc.loan.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    // GET /api/users/program/{program} - Obtener usuarios por programa académico
    @GetMapping("/program/{program}")
    public ResponseEntity<List<UserDTO>> getUsersByProgram(@PathVariable String program) {
        List<UserDTO> users = userService.getUsersByAcademicProgram(program);
        return ResponseEntity.ok(users);
    }

    // GET /api/users/{code}/validate - Validar si usuario está activo
    @GetMapping("/{code}/validate")
    public ResponseEntity<Map<String, Object>> validateUser(@PathVariable String code) {
        boolean isActive = userService.isUserActiveByCode(code);
        
        Map<String, Object> response = new HashMap<>();
        response.put("userCode", code);
        response.put("isValid", isActive);
        response.put("timestamp", LocalDateTime.now());
        
        if (isActive) {
            response.put("message", "Usuario activo y válido para préstamos");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Usuario no existe o está inactivo");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}