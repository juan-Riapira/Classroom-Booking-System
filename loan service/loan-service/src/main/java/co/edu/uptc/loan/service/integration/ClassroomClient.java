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
     * Verifica si un aula existe y está disponible consultando al Classroom-Service
     *
     * @param classroomId ID del aula
     * @return true si el aula existe y está disponible, false en caso contrario
     */
    public boolean isClassroomAvailable(Long classroomId) {

        if (!serviceEnabled) {
            System.out.println("⚠️ Classroom-Service DESHABILITADO en configuración");
            System.out.println("   → Asumiendo aula disponible (modo desarrollo)");
            return true;
        }

        try {
            // Construir URL para obtener aula por ID
            String url = String.format("%s/api/classrooms/%d", classroomServiceUrl, classroomId);

            System.out.println("🌐 Consultando Classroom-Service:");
            System.out.println("   URL: " + url);

            // Hacer llamada HTTP GET al classroom-service
            @SuppressWarnings("rawtypes")
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getBody() != null) {
                String status = (String) response.getBody().get("state");
                String name =(String) response.getBody().get("name"); 

                System.out.println("✅ Aula encontrada con estado: " + status+"nombre" +name);

                if ("AVAILABLE".equalsIgnoreCase(status)) {
                    return true;
                } else {
                    System.out.println("❌ Aula no disponible. Estado actual: " + status);
                    return false;
                }
            }

            System.out.println("⚠️ Respuesta vacía del servicio");
            return false;

        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("❌ ERROR 404: Aula ID '" + classroomId + "' no encontrada en Classroom-Service");
            return false;

        } catch (Exception e) {
            System.out.println("⚠️ ERROR conectando con Classroom-Service: " + e.getClass().getSimpleName());
            System.out.println("   Mensaje: " + e.getMessage());
            System.out.println("   → Política de fallback: RECHAZAR préstamo por seguridad");

            return false;
        }
    }
}
