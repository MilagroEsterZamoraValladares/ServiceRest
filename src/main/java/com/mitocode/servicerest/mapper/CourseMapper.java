package com.mitocode.servicerest.mapper;

import com.mitocode.servicerest.dto.CourseDTO;
import com.mitocode.servicerest.dto.DetailRegistrationDTO;
import com.mitocode.servicerest.entity.Course;
import com.mitocode.servicerest.entity.DetailRegistration;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CourseMapper {

    private final Function<DetailRegistration, DetailRegistrationDTO> detailToDTO = detail ->
            Optional.ofNullable(detail)
                    .map(d -> DetailRegistrationDTO.builder()
                            .id(d.getId())
                            .courseId(d.getCourse().getId())
                            .classroom(d.getClassroom())
                            .build())
                    .orElse(null);

    private final Function<Course, CourseDTO> entityToDTO = course ->
            Optional.ofNullable(course)
                    .map(c -> CourseDTO.builder()
                            .id(c.getId())
                            .name(c.getNameCourse())
                            .acronym(c.getAcronymCourse())
                            .status(c.getStateCourse())
                            .detailRegistration(
                                    Optional.ofNullable(c.getDetailRegistrationCourse())
                                            .map(details -> details.stream()
                                                    .map(detailToDTO)
                                                    .collect(Collectors.toList()))
                                            .orElse(List.of())
                            )
                            .build())
                    .orElse(null);

    private final Function<CourseDTO, Course> dtoToEntity = dto ->
            Optional.ofNullable(dto)
                    .map(d -> Course.builder()
                            .id(d.getId())
                            .nameCourse(d.getName())
                            .acronymCourse(d.getAcronym())
                            .stateCourse(Optional.ofNullable(d.getStatus()).orElse(true))
                            .build())
                    .orElse(null);

    public CourseDTO toDTO(Course course) {
        return entityToDTO.apply(course);
    }

    public Course toEntity(CourseDTO dto) {
        return dtoToEntity.apply(dto);
    }

    public List<CourseDTO> toDTOList(List<Course> courses) {
        return Optional.ofNullable(courses)
                .map(list -> list.stream()
                        .map(entityToDTO)
                        .collect(Collectors.toList()))
                .orElse(List.of());
    }
}