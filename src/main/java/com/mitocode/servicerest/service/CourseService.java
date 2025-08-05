package com.mitocode.servicerest.service;
import com.mitocode.servicerest.dto.CourseDTO;
import com.mitocode.servicerest.dto.CourseStudentsDTO;
import com.mitocode.servicerest.entity.Course;
import com.mitocode.servicerest.entity.Student;
import com.mitocode.servicerest.mapper.CourseMapper;
import com.mitocode.servicerest.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    private final Predicate<String> isNameValid = name ->
            Optional.ofNullable(name)
                    .filter(n -> !n.trim().isEmpty())
                    .isPresent();

    private final Predicate<String> isAcronymValid = acronym ->
            Optional.ofNullable(acronym)
                    .filter(a -> !a.trim().isEmpty() && a.length() <= 10)
                    .isPresent();

    private final Predicate<CourseDTO> isCourseValid = course ->
            isNameValid.test(course.getName()) && isAcronymValid.test(course.getAcronym());

    private final Function<Student, String> getStudentFullName = student ->
            Optional.ofNullable(student)
                    .map(s -> s.getFirstName() + " " + s.getLastName())
                    .orElse("");

    public List<CourseDTO> findAll() {
        return courseRepository.findAll()
                .stream()
                .map(courseMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<CourseDTO> findById(Integer id) {
        return Optional.ofNullable(id)
                .filter(i -> i > 0)
                .flatMap(courseRepository::findById)
                .map(courseMapper::toDTO);
    }

    public Optional<CourseDTO> save(CourseDTO courseDTO) {
        return Optional.ofNullable(courseDTO)
                .filter(isCourseValid)
                .map(courseMapper::toEntity)
                .map(courseRepository::save)
                .map(courseMapper::toDTO);
    }

    public Optional<CourseDTO> update(Integer id, CourseDTO courseDTO) {
        return findById(id)
                .filter(existing -> isCourseValid.test(courseDTO))
                .map(existing -> {
                    courseDTO.setId(id);
                    return courseMapper.toEntity(courseDTO);
                })
                .map(courseRepository::save)
                .map(courseMapper::toDTO);
    }

    public boolean deleteById(Integer id) {
        return findById(id)
                .map(course -> {
                    courseRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }

    public Map<String, List<String>> getCoursesWithStudentsMap() {
        return courseRepository.findActiveCoursesWithStudents()
                .stream()
                .filter(course -> Optional.ofNullable(course.getStateCourse()).orElse(false))
                .collect(Collectors.toMap(
                        Course::getNameCourse,
                        course -> Optional.ofNullable(course.getStudents())
                                .map(students -> students.stream()
                                        .map(getStudentFullName)
                                        .sorted()
                                        .collect(Collectors.toList()))
                                .orElse(List.of()),
                        (existing, replacement) -> existing
                ));
    }

    public List<CourseStudentsDTO> getCoursesWithStudentsList() {
        return courseRepository.findActiveCoursesWithStudents()
                .stream()
                .filter(course -> Optional.ofNullable(course.getStateCourse()).orElse(false))
                .map(course -> CourseStudentsDTO.builder()
                        .courseName(course.getNameCourse())
                        .students(Optional.ofNullable(course.getStudents())
                                .map(students -> students.stream()
                                        .map(getStudentFullName)
                                        .sorted()
                                        .collect(Collectors.toList()))
                                .orElse(List.of()))
                        .build())
                .collect(Collectors.toList());
    }

    public Map<String, Long> getCourseStatistics() {
        return courseRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        course -> course.getStateCourse() ? "Active" : "Inactive",
                        Collectors.counting()
                ));
    }

    public List<CourseDTO> findCoursesWithMostStudents(int limit) {
        return courseRepository.findActiveCoursesWithStudents()
                .stream()
                .filter(course -> course.getStateCourse())
                .sorted((c1, c2) -> {
                    int size1 = Optional.ofNullable(c1.getStudents()).map(Set::size).orElse(0);
                    int size2 = Optional.ofNullable(c2.getStudents()).map(Set::size).orElse(0);
                    return Integer.compare(size2, size1);
                })
                .limit(limit)
                .map(courseMapper::toDTO)
                .collect(Collectors.toList());
    }
}