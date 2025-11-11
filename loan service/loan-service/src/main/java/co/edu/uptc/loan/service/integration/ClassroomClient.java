package co.edu.uptc.loan.service.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
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

    @Value("${classroom.service.enabled:true}")
    private boolean serviceEnabled;

    /**
     * 🔍 Verifica si el aula existe y está disponible
     */
    public boolean isClassroomAvailable(Long classroomId) {

        if (!serviceEnabled) {
            System.out.println("⚠️ Classroom-Service DESHABILITADO (modo desarrollo)");
            return true;
        }

        try {
            // ✅ CORREGIDO: ahora llama al GET correcto
            String url = String.format("%s/api/classrooms/%d", classroomServiceUrl, classroomId);

            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getBody() != null) {
                String status = (String) response.getBody().get("state");

                return "AVAILABLE".equalsIgnoreCase(status);
            }
            return false;

        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 🔄 Actualiza el estado del aula
     */
    public boolean updateClassroomStatus(Long classroomId, String newStatus) {

        if (!serviceEnabled) {
            return true;
        }

        try {
            // PUT correcto
            String url = String.format("%s/api/classrooms/%d/status", classroomServiceUrl, classroomId);

            Map<String, String> body = Map.of("state", newStatus);
            org.springframework.http.HttpEntity<Map<String, String>> requestEntity = new org.springframework.http.HttpEntity<>(
                    body);

            restTemplate.exchange(url, org.springframework.http.HttpMethod.PUT, requestEntity, Void.class);
            return true;

        } catch (HttpClientErrorException.NotFound e) {
            return false;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtiene la lista de todas las aulas desde el Classroom-Service
     */
    public List<Map<String, Object>> getAllClassrooms() {

        if (!serviceEnabled) {
            return List.of();
        }

        try {
            String url = String.format("%s/api/classrooms", classroomServiceUrl);

            ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);

            if (response.getBody() != null) {
                return response.getBody();
            }

            return List.of();

        } catch (Exception e) {
            return List.of();
        }
    }

    /**
     * Marca un aula como "AVAILABLE" cuando se elimina un préstamo
     */
    public boolean releaseClassroom(Long classroomId) {

        return updateClassroomStatus(classroomId, "AVAILABLE");
    }
}
