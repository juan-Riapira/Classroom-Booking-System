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

            System.out.println("🌐 Consultando Classroom-Service:");
            System.out.println("   URL: " + url);

            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getBody() != null) {
                String status = (String) response.getBody().get("state");
                System.out.println("✅ Aula encontrada con estado: " + status);

                return "AVAILABLE".equalsIgnoreCase(status);
            }

            System.out.println("⚠️ Respuesta vacía del servicio");
            return false;

        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("❌ Aula no encontrada (ID=" + classroomId + ")");
            return false;
        } catch (Exception e) {
            System.out.println("⚠️ Error consultando aula: " + e.getMessage());
            return false;
        }
    }

    /**
     * 🔄 Actualiza el estado del aula
     */
    public boolean updateClassroomStatus(Long classroomId, String newStatus) {

        if (!serviceEnabled) {
            System.out.println("⚠️ Classroom-Service DESHABILITADO (modo desarrollo)");
            return true;
        }

        try {
            // PUT correcto
            String url = String.format("%s/api/classrooms/%d/status", classroomServiceUrl, classroomId);

            Map<String, String> body = Map.of("state", newStatus);
            org.springframework.http.HttpEntity<Map<String, String>> requestEntity = new org.springframework.http.HttpEntity<>(
                    body);

            restTemplate.exchange(url, org.springframework.http.HttpMethod.PUT, requestEntity, Void.class);

            System.out.println("✅ Aula " + classroomId + " actualizada a estado: " + newStatus);
            return true;

        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("❌ ERROR 404: Aula ID '" + classroomId + "' no encontrada");
            return false;

        } catch (Exception e) {
            System.out.println("⚠️ ERROR al actualizar aula: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene la lista de todas las aulas desde el Classroom-Service
     */
    public List<Map<String, Object>> getAllClassrooms() {

        if (!serviceEnabled) {
            System.out.println("⚠️ Classroom-Service DESHABILITADO en configuración");
            System.out.println("   → Retornando lista vacía (modo desarrollo)");
            return List.of();
        }

        try {
            String url = String.format("%s/api/classrooms", classroomServiceUrl);

            System.out.println("🌐 Consultando todas las aulas en Classroom-Service:");
            System.out.println("   URL: " + url);

            ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);

            if (response.getBody() != null) {
                System.out.println("✅ Aulas recibidas: " + response.getBody().size());
                return response.getBody();
            }

            System.out.println("⚠️ Respuesta vacía del servicio de aulas");
            return List.of();

        } catch (Exception e) {
            System.out.println("⚠️ ERROR al consultar aulas: " + e.getClass().getSimpleName());
            System.out.println("   Mensaje: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Marca un aula como "AVAILABLE" cuando se elimina un préstamo
     */
    public boolean releaseClassroom(Long classroomId) {
        System.out.println("🔄 Actualizando aula " + classroomId + " a estado AVAILABLE...");

        return updateClassroomStatus(classroomId, "AVAILABLE");
    }
}
