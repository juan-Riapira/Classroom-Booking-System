package co.edu.uptc.loan.service.controller;

import co.edu.uptc.loan.service.dto.LoanDTO;
import co.edu.uptc.loan.service.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@CrossOrigin(origins = "*")
public class LoanController {

    @Autowired
    private LoanService loanService;

    // POST /api/loans - Crear préstamo
    @PostMapping
    public ResponseEntity<LoanDTO> createLoan(@RequestBody LoanDTO loanDTO) {
        LoanDTO newLoan = loanService.createLoan(loanDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newLoan);
    }

    // GET /api/loans - Obtener todos los préstamos
    @GetMapping
    public ResponseEntity<List<LoanDTO>> getAllLoans() {
        List<LoanDTO> loans = loanService.getAllLoans();
        return ResponseEntity.ok(loans);
    }

    // GET /api/loans/user/{userCode} - Préstamos por usuario
    @GetMapping("/user/{userCode}")
    public ResponseEntity<List<LoanDTO>> getLoansByUser(@PathVariable String userCode) {
        List<LoanDTO> loans = loanService.getLoansByUser(userCode);
        return ResponseEntity.ok(loans);
    }

    // GET /api/loans/classroom/{classroomCode} - Préstamos por aula
    @GetMapping("/classroom/{classroomCode}")
    public ResponseEntity<List<LoanDTO>> getLoansByClassroom(@PathVariable String classroomCode) {
        List<LoanDTO> loans = loanService.getLoansByClassroom(classroomCode);
        return ResponseEntity.ok(loans);
    }

    // PUT /api/loans/{id} - Actualizar préstamo
    @PutMapping("/{id}")
    public ResponseEntity<LoanDTO> updateLoan(@PathVariable Long id, @RequestBody LoanDTO loanDTO) {
        LoanDTO updatedLoan = loanService.updateLoan(id, loanDTO);
        return ResponseEntity.ok(updatedLoan);
    }

    // DELETE /api/loans/{id} - Eliminar préstamo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
        loanService.deleteLoan(id);
        return ResponseEntity.noContent().build();
    }

    // PATCH /api/loans/{id}/status - Cambiar estado
    @PatchMapping("/{id}/status")
    public ResponseEntity<LoanDTO> changeStatus(@PathVariable Long id, @RequestParam String status) {
        LoanDTO updatedLoan = loanService.changeStatus(id, status);
        return ResponseEntity.ok(updatedLoan);
    }

    // GET /api/loans/status/{status} - Búsqueda por estado
    @GetMapping("/status/{status}")
    public ResponseEntity<List<LoanDTO>> getLoansByStatus(@PathVariable String status) {
        List<LoanDTO> loans = loanService.getLoansByStatus(status);
        return ResponseEntity.ok(loans);
    }

    // GET /api/loans/date-range - Búsqueda por rango de fechas
    @GetMapping("/date-range")
    public ResponseEntity<List<LoanDTO>> getLoansByDateRange(@RequestParam String startDate, @RequestParam String endDate) {
        List<LoanDTO> loans = loanService.getLoansByDateRange(startDate, endDate);
        return ResponseEntity.ok(loans);
    }

    // === ENDPOINTS ESPECÍFICOS PARA GESTIÓN DE ESTADOS ===
    
    // PATCH /api/loans/{id}/activate - Activar préstamo reservado
    @PatchMapping("/{id}/activate")
    public ResponseEntity<LoanDTO> activateLoan(@PathVariable Long id) {
        LoanDTO activatedLoan = loanService.activateLoan(id);
        return ResponseEntity.ok(activatedLoan);
    }

    // PATCH /api/loans/{id}/cancel - Cancelar préstamo
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<LoanDTO> cancelLoan(@PathVariable Long id) {
        LoanDTO cancelledLoan = loanService.cancelLoan(id);
        return ResponseEntity.ok(cancelledLoan);
    }

    // GET /api/loans/active - Préstamos activos
    @GetMapping("/active")
    public ResponseEntity<List<LoanDTO>> getActiveLoans() {
        List<LoanDTO> loans = loanService.getActiveLoans();
        return ResponseEntity.ok(loans);
    }

    // GET /api/loans/reserved - Préstamos reservados (pendientes)
    @GetMapping("/reserved")
    public ResponseEntity<List<LoanDTO>> getReservedLoans() {
        List<LoanDTO> loans = loanService.getReservedLoans();
        return ResponseEntity.ok(loans);
    }

    // GET /api/loans/cancelled - Préstamos cancelados
    @GetMapping("/cancelled")
    public ResponseEntity<List<LoanDTO>> getCancelledLoans() {
        List<LoanDTO> loans = loanService.getCancelledLoans();
        return ResponseEntity.ok(loans);
    }
}