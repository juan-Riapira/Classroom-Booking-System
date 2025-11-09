package co.edu.uptc.reporting.service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uptc.reporting.service.dto.ClassroomFrequencyDTO;
import co.edu.uptc.reporting.service.dto.HourFrequencyDTO;
import co.edu.uptc.reporting.service.dto.MonthlyReportDTO;
import co.edu.uptc.reporting.service.dto.WeeklyReportDTO;
import co.edu.uptc.reporting.service.service.ReportingService;

@RestController
@RequestMapping("/api/reports")
public class ReportingController {

    private final ReportingService reportingService;

    public ReportingController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }

    @GetMapping("/peak-hours")
    public ResponseEntity<List<HourFrequencyDTO>> peakHours(@RequestParam(defaultValue = "3") int top) {
        return ResponseEntity.ok(reportingService.getPeakHours(top));
    }

    @GetMapping("/low-hours")
    public ResponseEntity<List<HourFrequencyDTO>> lowHours(@RequestParam(defaultValue = "3") int top) {
        return ResponseEntity.ok(reportingService.getLowHours(top));
    }

    @GetMapping("/weekly-by-program")
    public ResponseEntity<List<WeeklyReportDTO>> weeklyByProgram() {
        return ResponseEntity.ok(reportingService.getWeeklyByProgram());
    }

    @GetMapping("/monthly-by-program")
    public ResponseEntity<List<MonthlyReportDTO>> monthlyByProgram() {
        return ResponseEntity.ok(reportingService.getMonthlyByProgram());
    }

    @GetMapping("/classroom-frequency")
    public ResponseEntity<List<ClassroomFrequencyDTO>> classroomFrequency() {
        return ResponseEntity.ok(reportingService.getClassroomFrequency());
    }
}
