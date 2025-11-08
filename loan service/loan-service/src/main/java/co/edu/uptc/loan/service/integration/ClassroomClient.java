package co.edu.uptc.loan.service.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;

/**
 * Cliente para comunicarse con el Classroom-Service
 * Verifica existencia y disponibilidad de aulas antes de crear préstamos
 */
@Component
public class ClassroomClient {

    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${classroom.service.url:http://localhost:8081}")
    private String classroomServiceUrl;
    
    @Value("${classroom.service.enabled:false}")
    private boolean serviceEnabled;

    /**
     * Verifica si un aula está disponible consultando al Classroom-Service
     * 
     * @param classroomCode Código/nombre del aula
     * @param date Fecha en formato YYYY-MM-DD
     * @param startTime Hora inicio en formato HH:mm:ss
     * @param endTime Hora fin en formato HH:mm:ss
     * @return true si el aula existe y está disponible, false en caso contrario
     */
    public boolean isClassroomAvailable(String classroomCode, String date, String startTime, String endTime) {
        
        if (!serviceEnabled) {
            System.out.println("⚠️ Classroom-Service DESHABILITADO en configuración");
            System.out.println("   → Asumiendo aula disponible (modo desarrollo)");
            return true;
        }
        
        try {
            // Construir URL del endpoint de disponibilidad
            String url = String.format(
                "%s/api/classrooms/%s/available?date=%s&start=%s&end=%s",
                classroomServiceUrl, classroomCode, date, startTime, endTime
            );

            System.out.println("🌐 Consultando Classroom-Service:");
            System.out.println("   URL: " + url);
            
            // Hacer llamada HTTP GET al classroom-service
            @SuppressWarnings("rawtypes")
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            
            if (response.getBody() != null) {
                Boolean available = (Boolean) response.getBody().get("available");
                String reason = (String) response.getBody().get("reason");
                
                if (Boolean.TRUE.equals(available)) {
                    System.out.println("✅ Respuesta: Aula '" + classroomCode + "' DISPONIBLE");
                    return true;
                } else {
                    System.out.println("❌ Respuesta: Aula '" + classroomCode + "' NO DISPONIBLE");
                    System.out.println("   Razón: " + (reason != null ? reason : "No especificada"));
                    return false;
                }
            }
            
            System.out.println("⚠️ Respuesta vacía del servicio");
            return false;
            
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("❌ ERROR 404: Aula '" + classroomCode + "' no encontrada en Classroom-Service");
            return false;
            
        } catch (Exception e) {
            System.out.println("⚠️ ERROR conectando con Classroom-Service: " + e.getClass().getSimpleName());
            System.out.println("   Mensaje: " + e.getMessage());
            System.out.println("   → Política de fallback: RECHAZAR préstamo por seguridad");
            
            // Política conservadora: si no se puede verificar, NO permitir el préstamo
            return false;
        }
    }
}