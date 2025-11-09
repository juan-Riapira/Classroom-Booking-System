package co.edu.uptc.reporting.service.dto;

public class MonthlyReportDTO {

    private String programa; // programa académico
    private int mes; // número del mes (1–12)
    private long cantidad; // cantidad de préstamos en ese mes para el programa

    public MonthlyReportDTO() {
    }

    public MonthlyReportDTO(String program, int month, long count) {
        this.programa = program;
        this.mes = month;
        this.cantidad = count;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String program) {
        this.programa = program;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int month) {
        this.mes = month;
    }

    public long getCantidad() {
        return cantidad;
    }

    public void setCantidad(long count) {
        this.cantidad = count;
    }

}
