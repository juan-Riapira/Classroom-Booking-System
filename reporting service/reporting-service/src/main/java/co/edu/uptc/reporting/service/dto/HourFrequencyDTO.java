package co.edu.uptc.reporting.service.dto;

public class HourFrequencyDTO {

    private int hour; // hora de inicio del préstamo (start_hour)
    private long frequency; // frecuencia de préstamos en esa hora

    public HourFrequencyDTO() {
    }

    public HourFrequencyDTO(int hour, long frequency) {
        this.hour = hour;
        this.frequency = frequency;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public long getFrequency() {
        return frequency;
    }

    public void setFrequency(long frequency) {
        this.frequency = frequency;
    }

}
