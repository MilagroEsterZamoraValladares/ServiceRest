package com.mitocode.servicerest.entity;

import jakarta.persistence.*;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "courses")

public class Course {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre del curso es obligatorio")
    @Size(max = 100)
    @Column(nullable = false)
    private String nameCourse;

    @NotBlank(message = "Las siglas son obligatorias")
    @Size(max = 10)
    @Column(nullable = false)
    private String acronymCourse;

    @Column(nullable = false)
    private Boolean stateCourse = true;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetailRegistration> detailRegistrationCourse;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "curso_estudiante",
            joinColumns = @JoinColumn(name = "curso_id"),
            inverseJoinColumns = @JoinColumn(name = "estudiante_id")
    )
    private Set<Student> students;
}