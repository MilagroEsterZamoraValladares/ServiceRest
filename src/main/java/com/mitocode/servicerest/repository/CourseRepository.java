package com.mitocode.servicerest.repository;
import com.mitocode.servicerest.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findByStatusTrue();

    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.students WHERE c.stateCourse = true")
    List<Course> findActiveCoursesWithStudents();
}