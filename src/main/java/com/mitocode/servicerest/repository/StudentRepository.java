package com.mitocode.servicerest.repository;
import com.mitocode.servicerest.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public  interface StudentRepository extends JpaRepository<Student, Integer>{
    Optional<Student> findByDni(String dni);

    @Query("SELECT s FROM Student s ORDER BY s.age DESC")
    List<Student> findAllOrderByAgeDesc();

    boolean existsByDni(String dni);

    @Query("SELECT s FROM Student s WHERE s.age BETWEEN ?1 AND ?2")
    List<Student> findByAgeBetween(Integer minAge, Integer maxAge);
}
