package co.edu.uptc.reporting.service.dto;

public class HourFrequencyDTO {

    private int hora; // hora de inicio del préstamo (start_hour)
    private long frecuencia; // frecuencia de préstamos en esa hora

    public HourFrequencyDTO() {
    }

    public HourFrequencyDTO(int hour, long frequency) {
        this.hora = hour;
        this.frecuencia = frequency;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hour) {
        this.hora = hour;
    }

    public long getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(long frequency) {
        this.frecuencia = frequency;
    }

}
