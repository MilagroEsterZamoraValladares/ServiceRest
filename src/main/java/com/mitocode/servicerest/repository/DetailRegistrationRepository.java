package com.mitocode.servicerest.repository;

import com.mitocode.servicerest.entity.DetailRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailRegistrationRepository extends JpaRepository<DetailRegistration, Integer> {
    List<DetailRegistration> findByCourseId(Integer courseId);
}