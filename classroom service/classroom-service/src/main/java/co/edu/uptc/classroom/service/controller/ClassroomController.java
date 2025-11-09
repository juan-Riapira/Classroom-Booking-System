package co.edu.uptc.classroom.service.controller;

import java.util.List;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;


import co.edu.uptc.classroom.service.dto.ClassroomRequest;
import co.edu.uptc.classroom.service.dto.ClassroomResponse;
import co.edu.uptc.classroom.service.services.ClassroomService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/classrooms")
public class ClassroomController {

    @Autowired
    private ClassroomService classroomService;

    @GetMapping()
    public List<ClassroomResponse> getAll() {
        return classroomService.getAll();
    }

    @GetMapping("/{id}")
    public ClassroomResponse getById(@PathVariable Long id) {
        return classroomService.getById(id);
    }

    @PostMapping
    public ClassroomResponse create(@Valid @RequestBody ClassroomRequest dto) {
        return classroomService.save(dto);
    }

    @PutMapping("/{id}")
    public ClassroomResponse update(@PathVariable Long id, @Valid @RequestBody ClassroomRequest dto) {
        return classroomService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        classroomService.delete(id);
    }
   
}
    