package co.edu.uptc.loan.service.controller;

import co.edu.uptc.loan.service.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {

    @Autowired
    private LoanService loanService;

    // GET /api/analytics/hour-frequency - Análisis por hora (orden cronológico)
    @GetMapping("/hour-frequency")
    public ResponseEntity<Map<String, Object>> getHourFrequency() {
        List<Object[]> data = loanService.getHourFrequency();
        
        Map<String, Object> response = new HashMap<>();
        response.put("title", "Frecuencia de préstamos por hora");
        response.put("description", "Número de préstamos realizados por cada hora del día (orden cronológico)");
        response.put("data", data);
        
        return ResponseEntity.ok(response);
    }

    // GET /api/analytics/hour-frequency/highest - Horarios con MAYOR frecuencia
    @GetMapping("/hour-frequency/highest")
    public ResponseEntity<Map<String, Object>> getHighestHourFrequency() {
        List<Object[]> data = loanService.getHourFrequencyHighest();
        
        Map<String, Object> response = new HashMap<>();
        response.put("title", "Horarios con MAYOR frecuencia de préstamos");
        response.put("description", "Horarios más utilizados para préstamos de aulas (de mayor a menor uso)");
        response.put("data", data);
        
        return ResponseEntity.ok(response);
    }

    // GET /api/analytics/hour-frequency/lowest - Horarios con MENOR frecuencia ⭐ NUEVO
    @GetMapping("/hour-frequency/lowest")
    public ResponseEntity<Map<String, Object>> getLowestHourFrequency() {
        List<Object[]> data = loanService.getHourFrequencyLowest();
        
        Map<String, Object> response = new HashMap<>();
        response.put("title", "Horarios con MENOR frecuencia de préstamos");
        response.put("description", "Horarios menos utilizados para préstamos de aulas (de menor a mayor uso)");
        response.put("data", data);
        
        return ResponseEntity.ok(response);
    }

    // GET /api/analytics/week-frequency - Análisis por semana
    @GetMapping("/week-frequency")
    public ResponseEntity<Map<String, Object>> getWeekFrequency() {
        List<Object[]> data = loanService.getWeekFrequency();
        
        Map<String, Object> response = new HashMap<>();
        response.put("title", "Frecuencia de préstamos por semana");
        response.put("description", "Número de préstamos realizados por cada semana del año");
        response.put("data", data);
        
        return ResponseEntity.ok(response);
    }

    // GET /api/analytics/month-frequency - Análisis por mes
    @GetMapping("/month-frequency")
    public ResponseEntity<Map<String, Object>> getMonthFrequency() {
        List<Object[]> data = loanService.getMonthFrequency();
        
        Map<String, Object> response = new HashMap<>();
        response.put("title", "Frecuencia de préstamos por mes");
        response.put("description", "Número de préstamos realizados por cada mes del año");
        response.put("data", data);
        
        return ResponseEntity.ok(response);
    }

    // GET /api/analytics/summary - Resumen general
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getAnalyticsSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("hourFrequency", loanService.getHourFrequency());
        summary.put("weekFrequency", loanService.getWeekFrequency());
        summary.put("monthFrequency", loanService.getMonthFrequency());
        summary.put("description", "Resumen completo de analytics del sistema de préstamos");
        
        return ResponseEntity.ok(summary);
    }
}