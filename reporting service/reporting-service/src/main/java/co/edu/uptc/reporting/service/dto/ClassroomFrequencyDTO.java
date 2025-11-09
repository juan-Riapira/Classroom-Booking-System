package co.edu.uptc.reporting.service.dto;

public class ClassroomFrequencyDTO {

    private String aula_inf; // código o nombre del aula (classroom_code)
    private long frecuencia; // frecuencia de préstamos en esa aula

    public ClassroomFrequencyDTO() {
    }

    public ClassroomFrequencyDTO(String classroom, long frequency) {
        this.aula_inf = classroom;
        this.frecuencia = frequency;
    }

    public String getAula_inf() {
        return aula_inf;
    }

    public void setAula_inf(String classroom) {
        this.aula_inf = classroom;
    }

    public long getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(long frequency) {
        this.frecuencia = frequency;
    }

}
