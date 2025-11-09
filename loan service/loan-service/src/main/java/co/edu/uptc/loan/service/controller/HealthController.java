package co.edu.uptc.loan.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import co.edu.uptc.loan.service.repository.UserRepository;
import co.edu.uptc.loan.service.repository.LoanRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para verificar el estado de salud del servicio
 */
@RestController
@RequestMapping("/api/health")
public class HealthController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private LoanRepository loanRepository;
    
    @Autowired
    private DataSource dataSource;
    
    @Value("${spring.application.name}")
    private String serviceName;

    /**
     * Endpoint básico de health check
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("service", serviceName);
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("version", "1.0.0");
        
        return ResponseEntity.ok(health);
    }

    /**
     * Endpoint detallado de health check
     */
    @GetMapping("/detailed")
    public ResponseEntity<Map<String, Object>> detailedHealth() {
        Map<String, Object> health = new HashMap<>();
        health.put("service", serviceName);
        health.put("timestamp", LocalDateTime.now());
        
        // Verificar base de datos
        boolean dbHealthy = checkDatabaseHealth();
        health.put("database", dbHealthy ? "UP" : "DOWN");
        
        // Contar registros
        try {
            long userCount = userRepository.count();
            long loanCount = loanRepository.count();
            
            Map<String, Long> stats = new HashMap<>();
            stats.put("users", userCount);
            stats.put("loans", loanCount);
            health.put("statistics", stats);
            
        } catch (Exception e) {
            health.put("statistics", "ERROR: " + e.getMessage());
        }
        
        // Estado general
        health.put("status", dbHealthy ? "UP" : "DEGRADED");
        
        return ResponseEntity.ok(health);
    }

    /**
     * Endpoint para verificar conectividad con servicios externos
     */
    @GetMapping("/dependencies") 
    public ResponseEntity<Map<String, Object>> checkDependencies() {
        Map<String, Object> deps = new HashMap<>();
        deps.put("timestamp", LocalDateTime.now());
        
        // Verificar base de datos
        deps.put("database", checkDatabaseHealth() ? "UP" : "DOWN");
        
        // TODO: Agregar verificación de Classroom Service cuando esté disponible
        deps.put("classroom-service", "NOT_IMPLEMENTED");
        
        return ResponseEntity.ok(deps);
    }
    
    private boolean checkDatabaseHealth() {
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(5); // timeout 5 segundos
        } catch (Exception e) {
            return false;
        }
    }
}