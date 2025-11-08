package co.edu.uptc.classroom.service.repository;

import co.edu.uptc.classroom.service.model.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    
    /**
     * Busca un aula por su nombre/código
     */
    Optional<Classroom> findByName(String name);
}
