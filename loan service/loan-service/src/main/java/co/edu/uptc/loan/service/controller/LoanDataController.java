package co.edu.uptc.loan.service.controller;

import co.edu.uptc.loan.service.dto.LoanDTO;
import co.edu.uptc.loan.service.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * LoanDataController - Provee datos de préstamos para reporting-service
 */
@RestController
@RequestMapping("/api/loans/data")
@CrossOrigin(origins = "*")
public class LoanDataController {

    @Autowired
    private LoanService loanService;

    //  Datos sin procesar - todos los préstamos
    @GetMapping("/raw")
    public ResponseEntity<List<LoanDTO>> getAllLoansRaw() {
        List<LoanDTO> loans = loanService.getAllLoans();
        return ResponseEntity.ok(loans);
    }

    // Filtro simple por rango de fechas (sin cálculos)
    @GetMapping("/by-date-range")
    public ResponseEntity<List<LoanDTO>> getLoansByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        List<LoanDTO> loans = loanService.getLoansByDateRange(startDate, endDate);
        return ResponseEntity.ok(loans);
    }
    
    // Filtro simple por estado 
    @GetMapping("/by-status")
    public ResponseEntity<List<LoanDTO>> getLoansByStatus(@RequestParam String status) {
        List<LoanDTO> loans = loanService.getLoansByStatus(status);
        return ResponseEntity.ok(loans);
    }
    
    // Filtro simple por usuario 
    @GetMapping("/by-user")
    public ResponseEntity<List<LoanDTO>> getLoansByUser(@RequestParam String userCode) {
        List<LoanDTO> loans = loanService.getLoansByUser(userCode);
        return ResponseEntity.ok(loans);
    }
    
    // Filtro simple por aula 
    @GetMapping("/by-classroom")
    public ResponseEntity<List<LoanDTO>> getLoansByClassroom(@RequestParam String classroomCode) {
        List<LoanDTO> loans = loanService.getLoansByClassroom(classroomCode);
        return ResponseEntity.ok(loans);
    }
    
    @GetMapping("/by-program")
    public ResponseEntity<List<LoanDTO>> getLoansByAcademicProgram(@RequestParam String academicProgram) {
        List<LoanDTO> loans = loanService.getLoansByAcademicProgram(academicProgram);
        return ResponseEntity.ok(loans);
    }
    
    // Préstamos activos 
    @GetMapping("/active")
    public ResponseEntity<List<LoanDTO>> getActiveLoans() {
        List<LoanDTO> loans = loanService.getActiveLoans();
        return ResponseEntity.ok(loans);
    }
    
    // Préstamos reservados 
    @GetMapping("/reserved")
    public ResponseEntity<List<LoanDTO>> getReservedLoans() {
        List<LoanDTO> loans = loanService.getReservedLoans();
        return ResponseEntity.ok(loans);
    }
    
    // Préstamos cancelados 
    @GetMapping("/cancelled")
    public ResponseEntity<List<LoanDTO>> getCancelledLoans() {
        List<LoanDTO> loans = loanService.getCancelledLoans();
        return ResponseEntity.ok(loans);
    }
}