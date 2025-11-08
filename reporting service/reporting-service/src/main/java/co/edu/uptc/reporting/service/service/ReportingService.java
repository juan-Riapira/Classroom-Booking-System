package co.edu.uptc.reporting.service.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.uptc.reporting.service.dto.ClassroomFrequencyDTO;
import co.edu.uptc.reporting.service.dto.HourFrequencyDTO;
import co.edu.uptc.reporting.service.dto.MonthlyReportDTO;
import co.edu.uptc.reporting.service.dto.WeeklyReportDTO;
import co.edu.uptc.reporting.service.repository.LoanRepository;
import co.edu.uptc.reporting.service.repository.projection.ClassroomFreqProjection;
import co.edu.uptc.reporting.service.repository.projection.HourFreqProjection;
import co.edu.uptc.reporting.service.repository.projection.MonthlyByProgramProjection;
import co.edu.uptc.reporting.service.repository.projection.WeeklyByProgramProjection;

@Service
public class ReportingService {

    private LoanRepository loanRepository;

    public ReportingService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public List<HourFrequencyDTO> getPeakHours(int topN) {
        List<HourFreqProjection> result = loanRepository.findHoursOrderByFrequencyDesc();

        List<HourFrequencyDTO> list = new ArrayList<>();

        for (int i = 0; i < result.size() && i < topN; i++) {
            HourFreqProjection projection = result.get(i);

            int hour = projection.getHour();
            long frequency = projection.getFrequency();

            HourFrequencyDTO dto = new HourFrequencyDTO(hour, frequency);
            list.add(dto);
        }
        return list;
    }

    public List<HourFrequencyDTO> getLowHours(int topN) {
        List<HourFreqProjection> result = loanRepository.findHoursOrderByFrequencyAsc();

        List<HourFrequencyDTO> list = new ArrayList<>();

        for (int i = 0; i < result.size() && i < topN; i++) {
            HourFreqProjection projection = result.get(i);

            int hour = projection.getHour();
            long frecuencia = projection.getFrequency();

            HourFrequencyDTO dto = new HourFrequencyDTO(hour, frecuencia);
            list.add(dto);
        }

        return list;
    }

    public List<WeeklyReportDTO> getWeeklyByProgram() {
        List<WeeklyByProgramProjection> result = loanRepository.findWeeklyByProgramAll();
        List<WeeklyReportDTO> list = new ArrayList<>();

        for (WeeklyByProgramProjection projection : result) {
            String program = projection.getProgram();
            int week = projection.getWeek();
            long totalLoans = projection.getCount();

            WeeklyReportDTO dto = new WeeklyReportDTO(program, week, totalLoans);
            list.add(dto);
        }

        return list;
    }

  public List<MonthlyReportDTO> getMonthlyByProgram() {
    
    List<MonthlyByProgramProjection> result = loanRepository.findMonthlyByProgramAll();
    List<MonthlyReportDTO> list = new ArrayList<>();

    for (MonthlyByProgramProjection projection : result) {
        String program = projection.getProgram();
        int month = projection.getMonth();
        long totalLoans = projection.getCount();

        MonthlyReportDTO dto = new MonthlyReportDTO(program, month, totalLoans);
        list.add(dto);
    }
    return list;

  }

  public List<ClassroomFrequencyDTO> getClassroomFrequency() {
    
    List<ClassroomFreqProjection> result = loanRepository.findClassroomFrequency();
    List<ClassroomFrequencyDTO> list = new ArrayList<>();

    for (ClassroomFreqProjection projection : result) {
        String classroom = projection.getClassroom();
        long frequency = projection.getFrequency();

        ClassroomFrequencyDTO dto = new ClassroomFrequencyDTO(classroom, frequency);
        list.add(dto);
    }
    return list;
   }
}
