package com.mitocode.servicerest.entity;

import jakarta.persistence.*;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 100)
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100)
    @Column(nullable = false)
    private String lastName;

    @NotBlank(message = "El DNI es obligatorio")
    @Size(min = 8, max = 8, message = "El DNI debe tener 8 dígitos")
    @Column(unique = true, nullable = false)
    private String dni;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 18, message = "La edad mínima es 18 años")
    @Max(value = 80, message = "La edad máxima es 80 años")
    private Integer age;

    @ManyToMany(mappedBy = "students", fetch = FetchType.LAZY)
    private Set<Course> courses;
}