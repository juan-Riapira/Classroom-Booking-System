package co.edu.uptc.loan.service.controller;

import co.edu.uptc.loan.service.dto.LoanDTO;
import co.edu.uptc.loan.service.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/loans/data")
@CrossOrigin(origins = "*")
public class LoanDataController {

    @Autowired
    private LoanService loanService;

    // GET /api/loans/data/raw - Todos los préstamos sin procesar
    @GetMapping("/raw")
    public ResponseEntity<List<LoanDTO>> getAllLoansRaw() {
        List<LoanDTO> loans = loanService.getAllLoans();
        return ResponseEntity.ok(loans);
    }

    // GET /api/loans/data/by-hour - Conteo básico por hora
    @GetMapping("/by-hour")
    public ResponseEntity<Map<Integer, Long>> getLoanCountByHour() {
        List<LoanDTO> loans = loanService.getAllLoans();
        Map<Integer, Long> hourCount = loans.stream()
                .collect(Collectors.groupingBy(
                    LoanDTO::getStartHour,
                    Collectors.counting()
                ));
        return ResponseEntity.ok(hourCount);
    }

    // GET /api/loans/data/by-week - Conteo básico por semana
    @GetMapping("/by-week")
    public ResponseEntity<Map<Integer, Long>> getLoanCountByWeek() {
        List<LoanDTO> loans = loanService.getAllLoans();
        Map<Integer, Long> weekCount = loans.stream()
                .collect(Collectors.groupingBy(
                    LoanDTO::getWeekNumber,
                    Collectors.counting()
                ));
        return ResponseEntity.ok(weekCount);
    }

    // GET /api/loans/data/by-month - Conteo básico por mes  
    @GetMapping("/by-month")
    public ResponseEntity<Map<Integer, Long>> getLoanCountByMonth() {
        List<LoanDTO> loans = loanService.getAllLoans();
        Map<Integer, Long> monthCount = loans.stream()
                .collect(Collectors.groupingBy(
                    LoanDTO::getMonthNumber,
                    Collectors.counting()
                ));
        return ResponseEntity.ok(monthCount);
    }

    // GET /api/loans/data/by-status - Conteo básico por estado
    @GetMapping("/by-status")
    public ResponseEntity<Map<String, Long>> getLoanCountByStatus() {
        List<LoanDTO> loans = loanService.getAllLoans();
        Map<String, Long> statusCount = loans.stream()
                .collect(Collectors.groupingBy(
                    LoanDTO::getStatus,
                    Collectors.counting()
                ));
        return ResponseEntity.ok(statusCount);
    }

    // GET /api/loans/data/by-date-range - Préstamos en rango de fechas
    @GetMapping("/by-date-range")
    public ResponseEntity<List<LoanDTO>> getLoansByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        List<LoanDTO> loans = loanService.getLoansByDateRange(startDate, endDate);
        return ResponseEntity.ok(loans);
    }

    // GET /api/loans/data/summary - Resumen básico de datos
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getDataSummary() {
        List<LoanDTO> loans = loanService.getAllLoans();
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalLoans", loans.size());
        summary.put("activeLoans", loans.stream().filter(l -> "ACTIVE".equals(l.getStatus())).count());
        summary.put("reservedLoans", loans.stream().filter(l -> "RESERVED".equals(l.getStatus())).count());
        summary.put("cancelledLoans", loans.stream().filter(l -> "CANCELLED".equals(l.getStatus())).count());
        
        // Rangos de fechas
        if (!loans.isEmpty()) {
            LocalDate minDate = loans.stream().map(LoanDTO::getLoanDate).min(LocalDate::compareTo).orElse(null);
            LocalDate maxDate = loans.stream().map(LoanDTO::getLoanDate).max(LocalDate::compareTo).orElse(null);
            summary.put("dateRange", Map.of("start", minDate, "end", maxDate));
        }
        
        return ResponseEntity.ok(summary);
    }
}