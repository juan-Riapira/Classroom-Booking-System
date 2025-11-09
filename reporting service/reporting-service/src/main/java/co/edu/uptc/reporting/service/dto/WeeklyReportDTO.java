package co.edu.uptc.reporting.service.dto;

public class WeeklyReportDTO {

    private String programa; // nombre del programa (ej. Ingeniería de Sistemas)
    private int semana; // número de la semana (ej. 23)
    private long cantidad; // cantidad de préstamos en esa semana para el programa

    public WeeklyReportDTO() {
    }

    public WeeklyReportDTO(String program, int week, long count) {
        this.programa = program;
        this.semana = week;
        this.cantidad = count;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String program) {
        this.programa = program;
    }

    public int getSemana() {
        return semana;
    }

    public void setSemana(int week) {
        this.semana = week;
    }

    public long getCantidad() {
        return cantidad;
    }

    public void setCantidad(long count) {
        this.cantidad = count;
    }

}
