package com.mitocode.servicerest.controller;

import com.mitocode.servicerest.dto.CourseDTO;
import com.mitocode.servicerest.dto.CourseStudentsDTO;
import com.mitocode.servicerest.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        return ResponseEntity.ok(courseService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable Integer id) {
        return courseService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@Valid @RequestBody CourseDTO courseDTO) {
        return courseService.save(courseDTO)
                .map(saved -> ResponseEntity.status(201).body(saved))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(
            @PathVariable Integer id,
            @Valid @RequestBody CourseDTO courseDTO) {
        return courseService.update(id, courseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Integer id) {
        return courseService.deleteById(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/with-students")
    public ResponseEntity<Map<String, List<String>>> getCoursesWithStudents() {
        return ResponseEntity.ok(courseService.getCoursesWithStudentsMap());
    }

    @GetMapping("/with-students-list")
    public ResponseEntity<List<CourseStudentsDTO>> getCoursesWithStudentsList() {
        return ResponseEntity.ok(courseService.getCoursesWithStudentsList());
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Long>> getCourseStatistics() {
        return ResponseEntity.ok(courseService.getCourseStatistics());
    }

    @GetMapping("/top-enrolled")
    public ResponseEntity<List<CourseDTO>> getTopEnrolledCourses(
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(courseService.findCoursesWithMostStudents(limit));
    }
}