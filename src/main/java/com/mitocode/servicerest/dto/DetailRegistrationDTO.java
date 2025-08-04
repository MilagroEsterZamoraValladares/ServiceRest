package com.mitocode.servicerest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailRegistrationDTO {
    private Integer id;
    private Integer courseId;

    @NotBlank(message = "El nombre del curso es obligatorio")
    @Size(max = 20)
    private String classroom;
}
