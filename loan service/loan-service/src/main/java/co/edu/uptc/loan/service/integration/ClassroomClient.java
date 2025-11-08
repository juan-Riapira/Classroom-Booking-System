package co.edu.uptc.loan.service.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

/**
 * Cliente simple para comunicarse con el Classroom-Service
 * Incluye fallback seguro cuando el servicio no está disponible
 */
@Component
public class ClassroomClient {

    @Autowired
    private RestTemplate restTemplate;
    
    // Configuración desde properties (con valores por defecto)
    @Value("${classroom.service.url:http://localhost:8081}")
    private String classroomServiceUrl;
    
    @Value("${classroom.service.enabled:false}")
    private boolean serviceEnabled;

    /**
     * Verifica si un aula está disponible en una fecha y horario específico
     */
    public boolean isClassroomAvailable(String classroomCode, String date, String startTime, String endTime) {
        
        // Si el servicio está deshabilitado, asume que está disponible
        if (!serviceEnabled) {
            System.out.println("📋 Classroom-Service deshabilitado - Asumiendo aula disponible: " + classroomCode);
            return true;
        }
        
        try {
            // Construir la URL para consultar disponibilidad
            String url = String.format(
                "%s/api/classrooms/%s/available?date=%s&start=%s&end=%s",
                classroomServiceUrl, classroomCode, date, startTime, endTime
            );

            System.out.println("🔍 Consultando disponibilidad: " + classroomCode + " en " + date);
            
            // Hacer la llamada HTTP al classroom-service
            ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
            
            // Verificar respuesta
            if (response.getBody() != null) {
                boolean available = response.getBody();
                System.out.println("✅ Respuesta classroom-service: aula " + 
                    (available ? "DISPONIBLE" : "OCUPADA"));
                return available;
            }
            
            // Si no hay respuesta válida, asumir no disponible
            return false;
            
        } catch (Exception e) {
            // Si hay error (servicio caído, timeout, etc.), usar fallback seguro
            System.out.println("⚠️ Error conectando con Classroom-Service: " + e.getMessage());
            System.out.println("🔄 Usando fallback: asumiendo aula disponible");
            return true; // Fallback seguro - no bloquear préstamos
        }
    }
}