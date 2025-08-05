package com.mitocode.servicerest.mapper;
import com.mitocode.servicerest.entity.Student;
import com.mitocode.servicerest.dto.StudentDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class StudentMapper {

    private final Function<Student, StudentDTO> entityToDTO = student ->
            Optional.ofNullable(student)
                    .map(s -> StudentDTO.builder()
                            .id(s.getId())
                            .firstName(s.getFirstName())
                            .lastName(s.getLastName())
                            .dni(s.getDni())
                            .age(s.getAge())
                            .build())
                    .orElse(null);

    private final Function<StudentDTO, Student> dtoToEntity = dto ->
            Optional.ofNullable(dto)
                    .map(d -> Student.builder()
                            .id(d.getId())
                            .firstName(d.getFirstName())
                            .lastName(d.getLastName())
                            .dni(d.getDni())
                            .age(d.getAge())
                            .build())
                    .orElse(null);

    public StudentDTO toDTO(Student student) {
        return entityToDTO.apply(student);
    }

    public Student toEntity(StudentDTO dto) {
        return dtoToEntity.apply(dto);
    }

    public List<StudentDTO> toDTOList(List<Student> students) {
        return Optional.ofNullable(students)
                .map(list -> list.stream()
                        .map(entityToDTO)
                        .collect(Collectors.toList()))
                .orElse(List.of());
    }
}