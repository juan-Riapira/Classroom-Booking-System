package co.edu.uptc.loan.service.service;

import co.edu.uptc.loan.service.dto.LoanDTO;
import co.edu.uptc.loan.service.model.Loan;
import co.edu.uptc.loan.service.repository.LoanRepository;
import co.edu.uptc.loan.service.exception.LoanServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;
    
    @Autowired
    private UserService userService;

    // Crear préstamo con validaciones profesionales
    public LoanDTO createLoan(LoanDTO loanDTO) {
        // 1. Validar que el usuario existe y está activo
        if (!userService.isUserActiveByCode(loanDTO.getUserCode())) {
            throw new LoanServiceException.UserNotActiveException(loanDTO.getUserCode());
        }

        // 2. Verificar conflictos de horario
        List<Loan> conflicts = loanRepository.findConflictingLoans(
            loanDTO.getClassroomCode(),
            loanDTO.getLoanDate(),
            loanDTO.getStartTime(),
            loanDTO.getEndTime()
        );

        if (!conflicts.isEmpty()) {
            throw new LoanServiceException.TimeConflictException(loanDTO.getClassroomCode());
        }

        // 3. Crear y configurar el préstamo
        Loan loan = convertToEntity(loanDTO);
        loan.setStatus(loanDTO.getStatus() != null ? loanDTO.getStatus() : "RESERVED");

        Loan savedLoan = loanRepository.save(loan);
        return convertToDTO(savedLoan);
    }

    // Obtener todos los préstamos
    public List<LoanDTO> getAllLoans() {
        return loanRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Obtener préstamos por usuario
    public List<LoanDTO> getLoansByUser(String userCode) {
        return loanRepository.findByUserCode(userCode)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Obtener préstamos por aula
    public List<LoanDTO> getLoansByClassroom(String classroomCode) {
        return loanRepository.findByClassroomCode(classroomCode)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Obtener préstamos por programa académico del usuario
    public List<LoanDTO> getLoansByAcademicProgram(String academicProgram) {
        return loanRepository.findByUser_AcademicProgram(academicProgram)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // === NUEVOS MÉTODOS CRUD ===
    
    // Actualizar préstamo
    public LoanDTO updateLoan(Long id, LoanDTO loanDTO) {
        Loan existingLoan = loanRepository.findById(id)
                .orElseThrow(() -> new LoanServiceException.LoanNotFoundException(id));
        
        // Validar usuario activo
        if (!userService.isUserActiveByCode(loanDTO.getUserCode())) {
            throw new LoanServiceException.UserNotActiveException(loanDTO.getUserCode());
        }
        
        // Verificar conflictos (excluyendo el préstamo actual)
        List<Loan> conflicts = loanRepository.findConflictingLoans(
            loanDTO.getClassroomCode(),
            loanDTO.getLoanDate(),
            loanDTO.getStartTime(),
            loanDTO.getEndTime()
        ).stream().filter(l -> !l.getId().equals(id)).toList();
        
        if (!conflicts.isEmpty()) {
            throw new LoanServiceException.TimeConflictException(loanDTO.getClassroomCode());
        }
        
        // Actualizar campos usando método helper
        updateEntityFromDTO(existingLoan, loanDTO);
        
        Loan updatedLoan = loanRepository.save(existingLoan);
        return convertToDTO(updatedLoan);
    }
    
    // Eliminar préstamo
    public void deleteLoan(Long id) {
        if (!loanRepository.existsById(id)) {
            throw new LoanServiceException.LoanNotFoundException(id);
        }
        loanRepository.deleteById(id);
    }
    
    // Cambiar estado de préstamo
    public LoanDTO changeStatus(Long id, String newStatus) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new LoanServiceException.LoanNotFoundException(id));
        
        if (!List.of("ACTIVE", "RESERVED", "CANCELLED").contains(newStatus)) {
            throw new LoanServiceException.InvalidStatusException(newStatus);
        }
        
        loan.setStatus(newStatus);
        Loan updatedLoan = loanRepository.save(loan);
        return convertToDTO(updatedLoan);
    }
    
    // Buscar por estado
    public List<LoanDTO> getLoansByStatus(String status) {
        return loanRepository.findByStatus(status)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Buscar por rango de fechas
    public List<LoanDTO> getLoansByDateRange(String startDate, String endDate) {
        return loanRepository.findByDateRange(
            java.time.LocalDate.parse(startDate),
            java.time.LocalDate.parse(endDate)
        ).stream()
         .map(this::convertToDTO)
         .collect(Collectors.toList());
    }
    
    // === MÉTODOS HELPER PARA GESTIÓN DE ESTADOS ===
    
    // Activar préstamo (de RESERVED a ACTIVE)
    public LoanDTO activateLoan(Long id) {
        return changeStatus(id, "ACTIVE");
    }
    
    // Cancelar préstamo
    public LoanDTO cancelLoan(Long id) {
        return changeStatus(id, "CANCELLED");
    }
    
    // Obtener préstamos activos
    public List<LoanDTO> getActiveLoans() {
        return loanRepository.findByStatus("ACTIVE")
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Obtener préstamos reservados (pendientes de activar)
    public List<LoanDTO> getReservedLoans() {
        return loanRepository.findByStatus("RESERVED")
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Obtener préstamos cancelados
    public List<LoanDTO> getCancelledLoans() {
        return loanRepository.findByStatus("CANCELLED")
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // === MÉTODOS DE CONVERSIÓN ENTITY ↔ DTO ===
    
    /**
     * Convierte Entity a DTO para respuestas API
     */
    private LoanDTO convertToDTO(Loan loan) {
        LoanDTO dto = new LoanDTO();
        dto.setId(loan.getId());
        dto.setUserCode(loan.getUserCode());
        dto.setClassroomCode(loan.getClassroomCode());
        dto.setLoanDate(loan.getLoanDate());
        dto.setStartTime(loan.getStartTime());
        dto.setEndTime(loan.getEndTime());
        dto.setPurpose(loan.getPurpose());
        dto.setStatus(loan.getStatus());
        
        return dto;
    }
    
    /**
     * Convierte DTO a Entity para persistencia
     */
    private Loan convertToEntity(LoanDTO dto) {
        Loan loan = new Loan();
        // ID solo se setea si existe (para actualizaciones)
        if (dto.getId() != null) {
            loan.setId(dto.getId());
        }
        loan.setUserCode(dto.getUserCode());
        loan.setClassroomCode(dto.getClassroomCode());
        loan.setLoanDate(dto.getLoanDate());
        loan.setStartTime(dto.getStartTime());
        loan.setEndTime(dto.getEndTime());
        loan.setPurpose(dto.getPurpose());
        loan.setStatus(dto.getStatus());
        return loan;
    }
    
    /**
     * Actualiza una entidad existente con datos del DTO
     */
    private void updateEntityFromDTO(Loan loan, LoanDTO dto) {
        loan.setUserCode(dto.getUserCode());
        loan.setClassroomCode(dto.getClassroomCode());
        loan.setLoanDate(dto.getLoanDate());
        loan.setStartTime(dto.getStartTime());
        loan.setEndTime(dto.getEndTime());
        loan.setPurpose(dto.getPurpose());
        loan.setStatus(dto.getStatus() != null ? dto.getStatus() : loan.getStatus());
    }
}