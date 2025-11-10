package co.edu.uptc.classroom.service.services;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uptc.classroom.service.dto.ClassroomRequest;
import co.edu.uptc.classroom.service.dto.ClassroomResponse;
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
                        .capacity(c.getCapacity())
                        .location(c.getLocation())
                        .state(c.getState())
                        .build())
                .toList();
    }

    
    public ClassroomResponse getById(Long id) {
        Classroom c = classroomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Classroom not found"));
        return ClassroomResponse.builder()
                .id(c.getId())
                .name(c.getName())
                .capacity(c.getCapacity())
                .location(c.getLocation())
                .state(c.getState())
                .build();
    }


    public ClassroomResponse save(ClassroomRequest dto) {
        Classroom classroom = Classroom.builder()
                .name(dto.getName())
                .capacity(dto.getCapacity())
                .location(dto.getLocation())
                .state(dto.getState())
                .build();

        Classroom saved = classroomRepository.save(classroom);
        return ClassroomResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .capacity(saved.getCapacity())
                .location(saved.getLocation())
                .state(saved.getState())
                .build();
    }

    public ClassroomResponse update(Long id, ClassroomRequest dto) {
        Classroom updated = classroomRepository.findById(id)
                .map(c -> {
                    c.setName(dto.getName());
                    c.setCapacity(dto.getCapacity());
                    c.setLocation(dto.getLocation());
                    c.setState(dto.getState());
                    return classroomRepository.save(c);
                })
                .orElseThrow(() -> new RuntimeException("Classroom not found"));

        return ClassroomResponse.builder()
                .id(updated.getId())
                .name(updated.getName())
                .capacity(updated.getCapacity())
                .location(updated.getLocation())
                .state(updated.getState())
                .build();
    }

    public void delete(Long id) {
        classroomRepository.deleteById(id);
    }


        public ClassroomResponse updateState(Long id, String newState) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Classroom not found"));

        classroom.setState(newState);
        Classroom updated = classroomRepository.save(classroom);

        return ClassroomResponse.builder()
                .id(updated.getId())
                .name(updated.getName())
                .capacity(updated.getCapacity())
                .location(updated.getLocation())
                .state(updated.getState())
                .build();
        }


}