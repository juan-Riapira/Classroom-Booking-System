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

    // Buscar préstamos por código de aula
    List<Loan> findByClassroomCode(Long classroomCode);

    // Verificar conflictos de horario en la misma aula y fecha
    @Query("SELECT l FROM Loan l WHERE l.classroomCode = :classroomCode " +
            "AND l.loanDate = :loanDate " +
            "AND ((l.startTime <= :startTime AND l.endTime > :startTime) " +
            "OR (l.startTime < :endTime AND l.endTime >= :endTime) " +
            "OR (l.startTime >= :startTime AND l.endTime <= :endTime))")
    List<Loan> findConflictingLoans(@Param("classroomCode") Long classroomCode,
            @Param("loanDate") LocalDate loanDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);

    // Búsquedas por estado
    List<Loan> findByStatus(String status);

    // Búsquedas por rango de fechas
    @Query("SELECT l FROM Loan l WHERE l.loanDate BETWEEN :startDate AND :endDate")
    List<Loan> findByDateRange(@Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // Buscar préstamos por programa académico del usuario
    @Query("SELECT l FROM Loan l WHERE l.academicProgram = :academicProgram")
    List<Loan> findByAcademicProgram(@Param("academicProgram") String academicProgram);

}