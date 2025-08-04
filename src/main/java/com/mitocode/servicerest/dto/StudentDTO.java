package com.mitocode.servicerest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDTO {
    private Integer id;

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 100)
    private String firstName;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100)
    private String lastName;

    @NotBlank(message = "DNI es obligatorio")
    @Size(min = 8, max = 8, message = "El DNI debe tener 8 dígitos")
    private String dni;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 18, message = "La edad mínima es 18 años")
    @Max(value = 80, message = "La edad máxima es 80 años")
    private Integer age;
}
