package co.edu.uptc.reporting.service.repository.projection;

public interface MonthlyByProgramProjection {
    String getProgram();     // programa académico
    Integer getMonth();      // número del mes (1–12)
    Long getCount();       // cantidad de préstamos en ese mes para el programa
}
