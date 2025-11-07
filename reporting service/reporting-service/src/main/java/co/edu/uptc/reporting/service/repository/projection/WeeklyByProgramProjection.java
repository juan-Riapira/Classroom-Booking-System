package co.edu.uptc.reporting.service.repository.projection;

public interface WeeklyByProgramProjection {
    String getProgram();     // nombre del programa (ej. Ingeniería de Sistemas)
    Integer getWeek();       // número de la semana (ej. 23)
    Long getCount();         // cantidad de préstamos en esa semana para el programa
}
