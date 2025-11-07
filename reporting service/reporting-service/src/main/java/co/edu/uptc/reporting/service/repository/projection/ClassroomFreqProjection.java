package co.edu.uptc.reporting.service.repository.projection;

public interface ClassroomFreqProjection {
    String getClassroom();   // código o nombre del aula (classroom_code)
    Long getFrequency();     // frecuencia de préstamos en ese aula
}
