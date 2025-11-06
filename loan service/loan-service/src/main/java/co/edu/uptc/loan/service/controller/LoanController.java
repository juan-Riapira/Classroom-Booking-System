package co.edu.uptc.loan.service.controller;

import co.edu.uptc.loan.service.dto.LoanDTO;
import co.edu.uptc.loan.service.model.Loan;
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
    public ResponseEntity<?> createLoan(@RequestBody LoanDTO loanDTO) {
        try {
            Loan newLoan = loanService.createLoan(loanDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(newLoan);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }

    // GET /api/loans - Obtener todos los préstamos
    @GetMapping
    public ResponseEntity<List<Loan>> getAllLoans() {
        List<Loan> loans = loanService.getAllLoans();
        return ResponseEntity.ok(loans);
    }

    // GET /api/loans/user/{userCode} - Préstamos por usuario
    @GetMapping("/user/{userCode}")
    public ResponseEntity<List<Loan>> getLoansByUser(@PathVariable String userCode) {
        List<Loan> loans = loanService.getLoansByUser(userCode);
        return ResponseEntity.ok(loans);
    }

    // GET /api/loans/classroom/{classroomCode} - Préstamos por aula
    @GetMapping("/classroom/{classroomCode}")
    public ResponseEntity<List<Loan>> getLoansByClassroom(@PathVariable String classroomCode) {
        List<Loan> loans = loanService.getLoansByClassroom(classroomCode);
        return ResponseEntity.ok(loans);
    }

    // PUT /api/loans/{id} - Actualizar préstamo
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLoan(@PathVariable Long id, @RequestBody LoanDTO loanDTO) {
        try {
            Loan updatedLoan = loanService.updateLoan(id, loanDTO);
            return ResponseEntity.ok(updatedLoan);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }

    // DELETE /api/loans/{id} - Eliminar préstamo
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLoan(@PathVariable Long id) {
        try {
            loanService.deleteLoan(id);
            return ResponseEntity.ok("Préstamo eliminado exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }

    // PATCH /api/loans/{id}/status - Cambiar estado
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> changeStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            Loan updatedLoan = loanService.changeStatus(id, status);
            return ResponseEntity.ok(updatedLoan);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }

    // GET /api/loans/status/{status} - Búsqueda por estado
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Loan>> getLoansByStatus(@PathVariable String status) {
        List<Loan> loans = loanService.getLoansByStatus(status);
        return ResponseEntity.ok(loans);
    }

    // GET /api/loans/date-range - Búsqueda por rango de fechas
    @GetMapping("/date-range")
    public ResponseEntity<?> getLoansByDateRange(@RequestParam String startDate, @RequestParam String endDate) {
        try {
            List<Loan> loans = loanService.getLoansByDateRange(startDate, endDate);
            return ResponseEntity.ok(loans);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: Formato de fecha inválido. Use YYYY-MM-DD");
        }
    }

    // === ENDPOINTS ESPECÍFICOS PARA GESTIÓN DE ESTADOS ===
    
    // PATCH /api/loans/{id}/activate - Activar préstamo reservado
    @PatchMapping("/{id}/activate")
    public ResponseEntity<?> activateLoan(@PathVariable Long id) {
        try {
            Loan activatedLoan = loanService.activateLoan(id);
            return ResponseEntity.ok(activatedLoan);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }

    // PATCH /api/loans/{id}/cancel - Cancelar préstamo
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<?> cancelLoan(@PathVariable Long id) {
        try {
            Loan cancelledLoan = loanService.cancelLoan(id);
            return ResponseEntity.ok(cancelledLoan);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }

    // GET /api/loans/active - Préstamos activos
    @GetMapping("/active")
    public ResponseEntity<List<Loan>> getActiveLoans() {
        List<Loan> loans = loanService.getActiveLoans();
        return ResponseEntity.ok(loans);
    }

    // GET /api/loans/reserved - Préstamos reservados (pendientes)
    @GetMapping("/reserved")
    public ResponseEntity<List<Loan>> getReservedLoans() {
        List<Loan> loans = loanService.getReservedLoans();
        return ResponseEntity.ok(loans);
    }

    // GET /api/loans/cancelled - Préstamos cancelados
    @GetMapping("/cancelled")
    public ResponseEntity<List<Loan>> getCancelledLoans() {
        List<Loan> loans = loanService.getCancelledLoans();
        return ResponseEntity.ok(loans);
    }
}