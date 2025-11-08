package co.edu.uptc.loan.service.repository;

import co.edu.uptc.loan.service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Buscar usuario por código institucional
    Optional<User> findByCode(String code);
    
    // Buscar usuarios activos
    List<User> findByActiveTrue();
    
    // Buscar por tipo de usuario (STUDENT o TEACHER)
    List<User> findByUserType(String userType);
    
    // Buscar usuarios por programa académico
    List<User> findByAcademicProgram(String academicProgram);
    
    // Verificar si existe un usuario con ese código
    boolean existsByCode(String code);
}