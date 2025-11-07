package co.edu.uptc.reporting.service.repository.projection;

public interface HourFreqProjection {
    Integer getHour();       // hora de inicio del préstamo (start_hour)
    Long getFrequency();     // frecuencia de préstamos en esa hora
}
