package co.edu.uptc.loan.service.repository;

import co.edu.uptc.loan.service.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    
    // Buscar préstamos por código de usuario
    List<Loan> findByUserCode(String userCode);
    
    // Buscar préstamos por código de aula
    List<Loan> findByClassroomCode(String classroomCode);
    
    // Buscar préstamos por fecha
    List<Loan> findByLoanDate(LocalDate loanDate);
    
    // Verificar conflictos de horario en la misma aula y fecha
    @Query("SELECT l FROM Loan l WHERE l.classroomCode = :classroomCode " +
           "AND l.loanDate = :loanDate " +
           "AND ((l.startTime <= :startTime AND l.endTime > :startTime) " +
           "OR (l.startTime < :endTime AND l.endTime >= :endTime) " +
           "OR (l.startTime >= :startTime AND l.endTime <= :endTime))")
    List<Loan> findConflictingLoans(@Param("classroomCode") String classroomCode,
                                   @Param("loanDate") LocalDate loanDate,
                                   @Param("startTime") LocalTime startTime,
                                   @Param("endTime") LocalTime endTime);
    
    // Buscar préstamos por semana
    List<Loan> findByWeekNumber(Integer weekNumber);
    
    // Buscar préstamos por mes
    List<Loan> findByMonthNumber(Integer monthNumber);
    
    // Consulta para análisis por hora (orden cronológico)
    @Query("SELECT l.startHour, COUNT(l) FROM Loan l GROUP BY l.startHour ORDER BY l.startHour")
    List<Object[]> getHourFrequency();
    
    // Horarios con MAYOR frecuencia (más usados primero)
    @Query("SELECT l.startHour, COUNT(l) as frequency FROM Loan l " +
           "GROUP BY l.startHour ORDER BY frequency DESC")
    List<Object[]> getHourFrequencyDesc();
    
    // Horarios con MENOR frecuencia (menos usados primero)
    @Query("SELECT l.startHour, COUNT(l) as frequency FROM Loan l " +
           "GROUP BY l.startHour ORDER BY frequency ASC")
    List<Object[]> getHourFrequencyAsc();
    
    // Consulta para análisis por semana
    @Query("SELECT l.weekNumber, COUNT(l) FROM Loan l GROUP BY l.weekNumber ORDER BY l.weekNumber")
    List<Object[]> getWeekFrequency();
    
    // Consulta para análisis por mes
    @Query("SELECT l.monthNumber, COUNT(l) FROM Loan l GROUP BY l.monthNumber ORDER BY l.monthNumber")
    List<Object[]> getMonthFrequency();
    
    // Búsquedas por estado
    List<Loan> findByStatus(String status);
    
    // Búsquedas por rango de fechas
    @Query("SELECT l FROM Loan l WHERE l.loanDate BETWEEN :startDate AND :endDate")
    List<Loan> findByDateRange(@Param("startDate") LocalDate startDate, 
                              @Param("endDate") LocalDate endDate);
}