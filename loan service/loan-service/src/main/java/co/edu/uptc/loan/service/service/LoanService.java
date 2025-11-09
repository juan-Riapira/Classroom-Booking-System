package co.edu.uptc.loan.service.service;

import co.edu.uptc.loan.service.dto.LoanDTO;
import co.edu.uptc.loan.service.model.Loan;
import co.edu.uptc.loan.service.repository.LoanRepository;
import co.edu.uptc.loan.service.exception.LoanServiceException;
import co.edu.uptc.loan.service.integration.ClassroomClient;
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
    
    @Autowired
    private ClassroomClient classroomClient;

    // Crear préstamo con validaciones profesionales
   public LoanDTO createLoan(LoanDTO loanDTO) {
    // 1️⃣ Validar que el usuario existe y está activo
    //if (!userService.isUserActiveByCode(loanDTO.getUserCode())) {
       // throw new LoanServiceException.UserNotActiveException(loanDTO.getUserCode());
 //   }

    // 2️⃣ Validar existencia y disponibilidad del aula con classroom-service
    // Ahora usamos el método que consulta por ID y verifica el estado "AVAILABLE"
    boolean aulaDisponible = classroomClient.isClassroomAvailable(loanDTO.getClassroomCode());

    if (!aulaDisponible) {
        throw new LoanServiceException.ClassroomNotAvailableException(
            "The classroom with ID " + loanDTO.getClassroomCode() + " is not available."
        );
    }

    // 3️⃣ Verificar conflictos de horario entre préstamos en esta misma aula
   
    //List<Loan> conflicts = loanRepository.findConflictingLoans(
       // loanDTO.getClassroomCode(),
     //   loanDTO.getLoanDate(),
       // loanDTO.getStartTime(),
       // loanDTO.getEndTime()
   // );

   // if (!conflicts.isEmpty()) {
     //   throw new LoanServiceException.TimeConflictException(
       //     "Classroom ID " + loanDTO.getClassroomCode() + " has time conflicts."
      ////  );
   // }

    // 4️⃣ Crear y configurar el préstamo
    Loan loan = convertToEntity(loanDTO);

    // Si no tiene estado definido, se marca como "RESERVED" por defecto
    loan.setStatus(loanDTO.getStatus() != null ? loanDTO.getStatus() : "RESERVED");

    // Guardar préstamo
    Loan savedLoan = loanRepository.save(loan);

    // 🔹 (Opcional): Aquí podrías notificar al ClassroomService para cambiar el estado del aula a "OCCUPIED"
    // classroomClient.updateClassroomStatus(loanDTO.getClassroomId(), "OCCUPIED");

    return convertToDTO(savedLoan);
}


    // Obtener todos los préstamos
    public List<LoanDTO> getAllLoans() {
        return loanRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Obtener préstamo por ID
    public LoanDTO getLoanById(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new LoanServiceException.LoanNotFoundException(id));
        return convertToDTO(loan);
    }

    // Obtener préstamos por usuario
    public List<LoanDTO> getLoansByUser(String userCode) {
        return loanRepository.findByUserCode(userCode)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Obtener préstamos por aula
    public List<LoanDTO> getLoansByClassroom(Long classroomCode) {
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