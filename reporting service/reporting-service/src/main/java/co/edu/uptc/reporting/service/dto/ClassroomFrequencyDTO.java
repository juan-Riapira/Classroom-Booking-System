package co.edu.uptc.reporting.service.dto;

public class ClassroomFrequencyDTO {

    private String classroom; // código o nombre del aula (classroom_code)
    private long frequency; // frecuencia de préstamos en esa aula

    public ClassroomFrequencyDTO() {
    }

    public ClassroomFrequencyDTO(String classroom, long frequency) {
        this.classroom = classroom;
        this.frequency = frequency;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public long getFrequency() {
        return frequency;
    }

    public void setFrequency(long frequency) {
        this.frequency = frequency;
    }

}
