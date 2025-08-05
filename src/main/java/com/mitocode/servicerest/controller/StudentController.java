package com.mitocode.servicerest.controller;

import com.mitocode.servicerest.dto.StudentDTO;
import com.mitocode.servicerest.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        return ResponseEntity.ok(studentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Integer id) {
        return studentService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody StudentDTO studentDTO) {
        return studentService.save(studentDTO)
                .map(saved -> ResponseEntity.status(201).body(saved))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(
            @PathVariable Integer id,
            @Valid @RequestBody StudentDTO studentDTO) {
        return studentService.update(id, studentDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Integer id) {
        return studentService.deleteById(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/ordered-by-age")
    public ResponseEntity<List<StudentDTO>> getStudentsOrderedByAge() {
        return ResponseEntity.ok(studentService.findAllOrderedByAgeDesc());
    }

    @GetMapping("/by-age-range")
    public ResponseEntity<List<StudentDTO>> getStudentsByAgeRange(
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge) {
        return ResponseEntity.ok(studentService.findByAgeRange(minAge, maxAge));
    }

    @GetMapping("/grouped-by-age")
    public ResponseEntity<Map<String, List<StudentDTO>>> getStudentsGroupedByAge() {
        return ResponseEntity.ok(studentService.groupStudentsByAgeRange());
    }
}