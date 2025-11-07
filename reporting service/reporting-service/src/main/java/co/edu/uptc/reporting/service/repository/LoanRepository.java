package co.edu.uptc.reporting.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import co.edu.uptc.reporting.service.model.LoanEntity;
import co.edu.uptc.reporting.service.repository.projection.ClassroomFreqProjection;
import co.edu.uptc.reporting.service.repository.projection.HourFreqProjection;
import co.edu.uptc.reporting.service.repository.projection.MonthlyByProgramProjection;
import co.edu.uptc.reporting.service.repository.projection.WeeklyByProgramProjection;

@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, Long> {


    @Query(value = "SELECT HOUR(start_time) AS hour, COUNT(*) AS frequency " +
            "FROM loan GROUP BY HOUR(start_time) ORDER BY frequency DESC", nativeQuery = true)
    List<HourFreqProjection> findHoursOrderByFrequencyDesc();

 
    @Query(value = "SELECT HOUR(start_time) AS hour, COUNT(*) AS frequency " +
            "FROM loan GROUP BY HOUR(start_time) ORDER BY frequency ASC", nativeQuery = true)
    List<HourFreqProjection> findHoursOrderByFrequencyAsc();

  
    @Query(value = "SELECT u.academic_program AS program, WEEK(l.loan_date) AS week, COUNT(*) AS count " +
            "FROM loan l JOIN user u ON l.user_code = u.code " +
            "GROUP BY u.academic_program, WEEK(l.loan_date) " +
            "ORDER BY u.academic_program, WEEK(l.loan_date)", nativeQuery = true)
    List<WeeklyByProgramProjection> findWeeklyByProgramAll();


    @Query(value = "SELECT u.academic_program AS program, MONTH(l.loan_date) AS month, COUNT(*) AS count " +
            "FROM loan l JOIN user u ON l.user_code = u.code " +
            "GROUP BY u.academic_program, MONTH(l.loan_date) " +
            "ORDER BY u.academic_program, MONTH(l.loan_date)", nativeQuery = true)
    List<MonthlyByProgramProjection> findMonthlyByProgramAll();

    @Query(value = "SELECT classroom_code AS classroom, COUNT(*) AS frequency " +
            "FROM loan GROUP BY classroom_code ORDER BY frequency DESC", nativeQuery = true)
    List<ClassroomFreqProjection> findClassroomFrequency();

}
