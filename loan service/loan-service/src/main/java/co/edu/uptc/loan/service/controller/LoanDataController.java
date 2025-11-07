package co.edu.uptc.loan.service.controller;

import co.edu.uptc.loan.service.dto.LoanDTO;
import co.edu.uptc.loan.service.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * LoanDataController - Solo provee datos sin procesar para reporting-service
 * 
 * DIRECTRIZ DEL DIRECTOR:
 * "Loan-Service no calcula ni analiza, solo provee la información base 
 * (los datos limpios y validados de los préstamos)."
 * 
 * RESPONSABILIDAD: Solo endpoints de datos sin procesar.
 * Los cálculos y análisis son responsabilidad del reporting-service.
 */
@RestController
@RequestMapping("/api/loans/data")
@CrossOrigin(origins = "*")
public class LoanDataController {

    @Autowired
    private LoanService loanService;

    // ✅ CORRECTO: Datos sin procesar - todos los préstamos
    @GetMapping("/raw")
    public ResponseEntity<List<LoanDTO>> getAllLoansRaw() {
        List<LoanDTO> loans = loanService.getAllLoans();
        return ResponseEntity.ok(loans);
    }

    // ✅ CORRECTO: Filtro simple por rango de fechas (sin cálculos)
    @GetMapping("/by-date-range")
    public ResponseEntity<List<LoanDTO>> getLoansByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        List<LoanDTO> loans = loanService.getLoansByDateRange(startDate, endDate);
        return ResponseEntity.ok(loans);
    }
    
    // ✅ CORRECTO: Filtro simple por estado (sin agrupaciones ni conteos)
    @GetMapping("/by-status")
    public ResponseEntity<List<LoanDTO>> getLoansByStatus(@RequestParam String status) {
        List<LoanDTO> loans = loanService.getLoansByStatus(status);
        return ResponseEntity.ok(loans);
    }
    
    // ✅ CORRECTO: Filtro simple por usuario (sin cálculos)
    @GetMapping("/by-user")
    public ResponseEntity<List<LoanDTO>> getLoansByUser(@RequestParam String userCode) {
        List<LoanDTO> loans = loanService.getLoansByUser(userCode);
        return ResponseEntity.ok(loans);
    }
    
    // ✅ CORRECTO: Filtro simple por aula (sin cálculos)
    @GetMapping("/by-classroom")
    public ResponseEntity<List<LoanDTO>> getLoansByClassroom(@RequestParam String classroomCode) {
        List<LoanDTO> loans = loanService.getLoansByClassroom(classroomCode);
        return ResponseEntity.ok(loans);
    }
    
    // ✅ CORRECTO: Préstamos activos (filtro simple, sin conteos)
    @GetMapping("/active")
    public ResponseEntity<List<LoanDTO>> getActiveLoans() {
        List<LoanDTO> loans = loanService.getActiveLoans();
        return ResponseEntity.ok(loans);
    }
    
    // ✅ CORRECTO: Préstamos reservados (filtro simple, sin conteos)
    @GetMapping("/reserved")
    public ResponseEntity<List<LoanDTO>> getReservedLoans() {
        List<LoanDTO> loans = loanService.getReservedLoans();
        return ResponseEntity.ok(loans);
    }
    
    // ✅ CORRECTO: Préstamos cancelados (filtro simple, sin conteos)
    @GetMapping("/cancelled")
    public ResponseEntity<List<LoanDTO>> getCancelledLoans() {
        List<LoanDTO> loans = loanService.getCancelledLoans();
        return ResponseEntity.ok(loans);
    }
}