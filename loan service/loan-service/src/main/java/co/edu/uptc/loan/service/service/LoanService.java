package co.edu.uptc.loan.service.service;

import co.edu.uptc.loan.service.dto.LoanDTO;
import co.edu.uptc.loan.service.model.Loan;
import co.edu.uptc.loan.service.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;
    
    @Autowired
    private UserService userService;

    // Crear préstamo
    public Loan createLoan(LoanDTO loanDTO) {
        // 1. Validar que el usuario existe y está activo
        if (!userService.isUserActiveByCode(loanDTO.getUserCode())) {
            throw new RuntimeException("El usuario con código " + loanDTO.getUserCode() + 
                                     " no existe o no está activo");
        }

        // 2. Verificar conflictos de horario
        List<Loan> conflicts = loanRepository.findConflictingLoans(
            loanDTO.getClassroomCode(),
            loanDTO.getLoanDate(),
            loanDTO.getStartTime(),
            loanDTO.getEndTime()
        );

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Ya existe un préstamo en ese horario para el aula " + 
                                     loanDTO.getClassroomCode());
        }

        // 3. Crear y configurar el préstamo
        Loan loan = new Loan();
        loan.setUserCode(loanDTO.getUserCode());
        loan.setClassroomCode(loanDTO.getClassroomCode());
        loan.setLoanDate(loanDTO.getLoanDate());
        loan.setStartTime(loanDTO.getStartTime());
        loan.setEndTime(loanDTO.getEndTime());
        loan.setPurpose(loanDTO.getPurpose());
        loan.setStatus(loanDTO.getStatus() != null ? loanDTO.getStatus() : "RESERVED");

        // 4. Calcular campos automáticos
        loan.setStartHour(loanDTO.getStartTime().getHour());
        loan.setDuration((int) Duration.between(loanDTO.getStartTime(), loanDTO.getEndTime()).toMinutes());
        loan.setWeekNumber(loanDTO.getLoanDate().get(WeekFields.of(Locale.getDefault()).weekOfYear()));
        loan.setMonthNumber(loanDTO.getLoanDate().getMonthValue());

        return loanRepository.save(loan);
    }

    // Obtener todos los préstamos
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    // Obtener préstamos por usuario
    public List<Loan> getLoansByUser(String userCode) {
        return loanRepository.findByUserCode(userCode);
    }

    // Obtener préstamos por aula
    public List<Loan> getLoansByClassroom(String classroomCode) {
        return loanRepository.findByClassroomCode(classroomCode);
    }

    // === NUEVOS MÉTODOS CRUD ===
    
    // Actualizar préstamo
    public Loan updateLoan(Long id, LoanDTO loanDTO) {
        Loan existingLoan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado con ID: " + id));
        
        // Validar usuario activo
        if (!userService.isUserActiveByCode(loanDTO.getUserCode())) {
            throw new RuntimeException("El usuario con código " + loanDTO.getUserCode() + 
                                     " no existe o no está activo");
        }
        
        // Verificar conflictos (excluyendo el préstamo actual)
        List<Loan> conflicts = loanRepository.findConflictingLoans(
            loanDTO.getClassroomCode(),
            loanDTO.getLoanDate(),
            loanDTO.getStartTime(),
            loanDTO.getEndTime()
        ).stream().filter(l -> !l.getId().equals(id)).toList();
        
        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Ya existe un préstamo en ese horario para el aula " + 
                                     loanDTO.getClassroomCode());
        }
        
        // Actualizar campos
        existingLoan.setUserCode(loanDTO.getUserCode());
        existingLoan.setClassroomCode(loanDTO.getClassroomCode());
        existingLoan.setLoanDate(loanDTO.getLoanDate());
        existingLoan.setStartTime(loanDTO.getStartTime());
        existingLoan.setEndTime(loanDTO.getEndTime());
        existingLoan.setPurpose(loanDTO.getPurpose());
        existingLoan.setStatus(loanDTO.getStatus() != null ? loanDTO.getStatus() : existingLoan.getStatus());
        
        // Recalcular campos automáticos
        existingLoan.setStartHour(loanDTO.getStartTime().getHour());
        existingLoan.setDuration((int) Duration.between(loanDTO.getStartTime(), loanDTO.getEndTime()).toMinutes());
        existingLoan.setWeekNumber(loanDTO.getLoanDate().get(WeekFields.of(Locale.getDefault()).weekOfYear()));
        existingLoan.setMonthNumber(loanDTO.getLoanDate().getMonthValue());
        
        return loanRepository.save(existingLoan);
    }
    
    // Eliminar préstamo
    public void deleteLoan(Long id) {
        if (!loanRepository.existsById(id)) {
            throw new RuntimeException("Préstamo no encontrado con ID: " + id);
        }
        loanRepository.deleteById(id);
    }
    
    // Cambiar estado de préstamo
    public Loan changeStatus(Long id, String newStatus) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado con ID: " + id));
        
        if (!List.of("ACTIVE", "RESERVED", "CANCELLED").contains(newStatus)) {
            throw new RuntimeException("Estado inválido. Use: ACTIVE, RESERVED, CANCELLED");
        }
        
        loan.setStatus(newStatus);
        return loanRepository.save(loan);
    }
    
    // Buscar por estado
    public List<Loan> getLoansByStatus(String status) {
        return loanRepository.findByStatus(status);
    }
    
    // Buscar por rango de fechas
    public List<Loan> getLoansByDateRange(String startDate, String endDate) {
        return loanRepository.findByDateRange(
            java.time.LocalDate.parse(startDate),
            java.time.LocalDate.parse(endDate)
        );
    }

    // === MÉTODOS PARA ANALYTICS ===
    
    // Frecuencia de uso por hora (orden cronológico)
    public List<Object[]> getHourFrequency() {
        return loanRepository.getHourFrequency();
    }

    // Horarios con MAYOR frecuencia de préstamos
    public List<Object[]> getHourFrequencyHighest() {
        return loanRepository.getHourFrequencyDesc();
    }

    // Horarios con MENOR frecuencia de préstamos
    public List<Object[]> getHourFrequencyLowest() {
        return loanRepository.getHourFrequencyAsc();
    }

    // Frecuencia de uso por semana
    public List<Object[]> getWeekFrequency() {
        return loanRepository.getWeekFrequency();
    }

    // Frecuencia de uso por mes
    public List<Object[]> getMonthFrequency() {
        return loanRepository.getMonthFrequency();
    }
    
    // === MÉTODOS HELPER PARA GESTIÓN DE ESTADOS ===
    
    // Activar préstamo (de RESERVED a ACTIVE)
    public Loan activateLoan(Long id) {
        return changeStatus(id, "ACTIVE");
    }
    
    // Cancelar préstamo
    public Loan cancelLoan(Long id) {
        return changeStatus(id, "CANCELLED");
    }
    
    // Obtener préstamos activos
    public List<Loan> getActiveLoans() {
        return loanRepository.findByStatus("ACTIVE");
    }
    
    // Obtener préstamos reservados (pendientes de activar)
    public List<Loan> getReservedLoans() {
        return loanRepository.findByStatus("RESERVED");
    }
    
    // Obtener préstamos cancelados
    public List<Loan> getCancelledLoans() {
        return loanRepository.findByStatus("CANCELLED");
    }
}