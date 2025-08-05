package com.mitocode.servicerest.service;
import com.mitocode.servicerest.dto.StudentDTO;
import com.mitocode.servicerest.entity.Student;
import com.mitocode.servicerest.mapper.StudentMapper;
import com.mitocode.servicerest.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    private final Predicate<String> isDniValid = dni ->
            Optional.ofNullable(dni)
                    .filter(d -> d.matches("\\d{8}"))
                    .isPresent();

    private final Predicate<Integer> isAgeValid = age ->
            Optional.ofNullable(age)
                    .filter(a -> a >= 18 && a <= 80)
                    .isPresent();

    private final Predicate<StudentDTO> isStudentValid = student ->
            isDniValid.test(student.getDni()) && isAgeValid.test(student.getAge());

    private final Function<Student, String> getFullName = student ->
            Optional.ofNullable(student)
                    .map(s -> s.getFirstName() + " " + s.getLastName())
                    .orElse("");

    public List<StudentDTO> findAll() {
        return studentRepository.findAll()
                .stream()
                .map(studentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<StudentDTO> findById(Integer id) {
        return Optional.ofNullable(id)
                .filter(i -> i > 0)
                .flatMap(studentRepository::findById)
                .map(studentMapper::toDTO);
    }

    public Optional<StudentDTO> save(StudentDTO studentDTO) {
        return Optional.ofNullable(studentDTO)
                .filter(isStudentValid)
                .filter(dto -> !studentRepository.existsByDni(dto.getDni()))
                .map(studentMapper::toEntity)
                .map(studentRepository::save)
                .map(studentMapper::toDTO);
    }

    public Optional<StudentDTO> update(Integer id, StudentDTO studentDTO) {
        return findById(id)
                .filter(existing -> isStudentValid.test(studentDTO))
                .filter(existing -> !studentRepository.existsByDni(studentDTO.getDni()) ||
                        existing.getDni().equals(studentDTO.getDni()))
                .map(existing -> {
                    studentDTO.setId(id);
                    return studentMapper.toEntity(studentDTO);
                })
                .map(studentRepository::save)
                .map(studentMapper::toDTO);
    }

    public boolean deleteById(Integer id) {
        return findById(id)
                .map(student -> {
                    studentRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }

    public List<StudentDTO> findAllOrderedByAgeDesc() {
        return studentRepository.findAllOrderByAgeDesc()
                .stream()
                .map(studentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<StudentDTO> findByAgeRange(Integer minAge, Integer maxAge) {
        return studentRepository.findAll()
                .stream()
                .filter(student -> Optional.ofNullable(minAge)
                        .map(min -> student.getAge() >= min)
                        .orElse(true))
                .filter(student -> Optional.ofNullable(maxAge)
                        .map(max -> student.getAge() <= max)
                        .orElse(true))
                .sorted((s1, s2) -> s2.getAge().compareTo(s1.getAge()))
                .map(studentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public java.util.Map<String, List<StudentDTO>> groupStudentsByAgeRange() {
        Function<Student, String> ageRangeClassifier = student -> {
            int age = student.getAge();
            if (age < 20) return "Under 20";
            if (age < 30) return "20-29";
            if (age < 40) return "30-39";
            return "40+";
        };

        return studentRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        ageRangeClassifier,
                        Collectors.mapping(
                                studentMapper::toDTO,
                                Collectors.toList()
                        )
                ));
    }
}