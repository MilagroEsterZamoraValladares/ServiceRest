package com.mitocode.servicerest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDTO {
    private Integer id;

    @NotBlank(message = "El nombre del curso es obligatorio")
    @Size(max = 100)
    private String name;

    @NotBlank(message = "Las siglas son obligatorias")
    @Size(max = 10)
    private String acronym;

    private Boolean status;
    private List<DetailRegistrationDTO> detailRegistration;
}