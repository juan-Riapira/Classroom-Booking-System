package co.edu.uptc.reporting.service.dto;

public class MonthlyReportDTO {

    private String program; // programa académico
    private int month; // número del mes (1–12)
    private long count; // cantidad de préstamos en ese mes para el programa

    public MonthlyReportDTO() {
    }

    public MonthlyReportDTO(String program, int month, long count) {
        this.program = program;
        this.month = month;
        this.count = count;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

}
