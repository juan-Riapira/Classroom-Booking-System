package co.edu.uptc.classroom.service.services;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import co.edu.uptc.classroom.service.dto.ClassroomRequest;
import co.edu.uptc.classroom.service.dto.ClassroomResponse;
import co.edu.uptc.classroom.service.exception.ClassroomNotFoundException;
import co.edu.uptc.classroom.service.exception.DuplicateClassroomCodeException;
import co.edu.uptc.classroom.service.model.Classroom;
import co.edu.uptc.classroom.service.repository.ClassroomRepository;


@Service
public class ClassroomService {
    @Autowired
    private ClassroomRepository classroomRepository;

    public List<ClassroomResponse> getAll() {
        return classroomRepository.findAll()
                .stream()
                .map(c -> ClassroomResponse.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .code(c.getCode())
                        .capacity(c.getCapacity())
                        .location(c.getLocation())
                        .state(c.getState())
                        .build())
                .toList();
    }

    
    public ClassroomResponse getById(Long id) {
        Classroom c = classroomRepository.findById(id)
                .orElseThrow(() -> new ClassroomNotFoundException(id));
        return ClassroomResponse.builder()
                .id(c.getId())
                .name(c.getName())
                .code(c.getCode())
                .capacity(c.getCapacity())
                .location(c.getLocation())
                .state(c.getState())
                .build();
    }


    public ClassroomResponse save(ClassroomRequest dto) {
        // Verificar si ya existe un aula con ese código
        if (classroomRepository.existsByCode(dto.getCode())) {
            throw new DuplicateClassroomCodeException(dto.getCode());
        }
        
        Classroom classroom = Classroom.builder()
                .name(dto.getName())
                .code(dto.getCode())
                .capacity(dto.getCapacity())
                .location(dto.getLocation())
                .state(dto.getState())
                .build();

        try {
            Classroom saved = classroomRepository.save(classroom);
            return ClassroomResponse.builder()
                    .id(saved.getId())
                    .name(saved.getName())
                    .code(saved.getCode())
                    .capacity(saved.getCapacity())
                    .location(saved.getLocation())
                    .state(saved.getState())
                    .build();
        } catch (DataIntegrityViolationException e) {
            // En caso de que la validación en BD falle por alguna razón
            if (e.getMessage().toLowerCase().contains("code")) {
                throw new DuplicateClassroomCodeException(dto.getCode());
            }
            throw e;
        }
    }

    public ClassroomResponse update(Long id, ClassroomRequest dto) {
        Classroom existing = classroomRepository.findById(id)
                .orElseThrow(() -> new ClassroomNotFoundException(id));
        
        // Verificar si el código ya existe en otra aula (no en la misma)
        Optional<Classroom> classroomWithCode = classroomRepository.findByCode(dto.getCode());
        if (classroomWithCode.isPresent() && !classroomWithCode.get().getId().equals(id)) {
            throw new DuplicateClassroomCodeException(dto.getCode());
        }
        
        try {
            existing.setName(dto.getName());
            existing.setCode(dto.getCode());
            existing.setCapacity(dto.getCapacity());
            existing.setLocation(dto.getLocation());
            existing.setState(dto.getState());
            
            Classroom updated = classroomRepository.save(existing);
            
            return ClassroomResponse.builder()
                    .id(updated.getId())
                    .name(updated.getName())
                    .code(updated.getCode())
                    .capacity(updated.getCapacity())
                    .location(updated.getLocation())
                    .state(updated.getState())
                    .build();
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().toLowerCase().contains("code")) {
                throw new DuplicateClassroomCodeException(dto.getCode());
            }
            throw e;
        }
    }

    public void delete(Long id) {
        classroomRepository.deleteById(id);
    }

}