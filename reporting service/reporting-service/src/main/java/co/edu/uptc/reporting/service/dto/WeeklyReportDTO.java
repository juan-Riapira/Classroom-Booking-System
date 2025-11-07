package co.edu.uptc.reporting.service.dto;

public class WeeklyReportDTO {

    private String program; // nombre del programa (ej. Ingeniería de Sistemas)
    private int week; // número de la semana (ej. 23)
    private long count; // cantidad de préstamos en esa semana para el programa

    public WeeklyReportDTO() {
    }

    public WeeklyReportDTO(String program, int week, long count) {
        this.program = program;
        this.week = week;
        this.count = count;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

}
