package co.edu.uptc.classroom.service.controller;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import co.edu.uptc.classroom.service.dto.ClassroomRequest;
import co.edu.uptc.classroom.service.dto.ClassroomResponse;
import co.edu.uptc.classroom.service.services.ClassroomService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/classrooms")
public class ClassroomController {

    @Autowired
    private ClassroomService classroomService;

    @GetMapping()
    public List<ClassroomResponse> getAll() {
        return classroomService.getAll();
    }

    @GetMapping("/{id}")
    public ClassroomResponse getById(@PathVariable Long id) {
        return classroomService.getById(id);
    }

    @PostMapping
    public ClassroomResponse create(@Valid @RequestBody ClassroomRequest dto) {
        return classroomService.save(dto);
    }

    @PutMapping("/{id}")
    public ClassroomResponse update(@PathVariable Long id, @Valid @RequestBody ClassroomRequest dto) {
        return classroomService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        classroomService.delete(id);
    }

    /**
     * Endpoint para verificar disponibilidad de aula
     * Usado por Loan-Service antes de crear préstamos
     * 
     * GET /api/classrooms/{name}/available?date=2025-11-10&start=08:00:00&end=10:00:00
     */
    @GetMapping("/{name}/available")
    public ResponseEntity<Map<String, Object>> checkAvailability(
            @PathVariable String name,
            @RequestParam String date,
            @RequestParam String start,
            @RequestParam String end) {
        
        Map<String, Object> response = new HashMap<>();
        
        System.out.println("🔍 Verificando disponibilidad de aula: " + name);
        System.out.println("   Fecha: " + date + " | Horario: " + start + " - " + end);
        
        boolean available = classroomService.isClassroomAvailable(name, date, start, end);
        
        response.put("available", available);
        response.put("classroomName", name);
        response.put("date", date);
        response.put("timeSlot", start + " - " + end);
        
        if (!available) {
            response.put("reason", "Aula no encontrada o no disponible");
            System.out.println("❌ Aula NO disponible");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        
        System.out.println("✅ Aula DISPONIBLE");
        return ResponseEntity.ok(response);
    }
}
    