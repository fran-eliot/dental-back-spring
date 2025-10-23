package com.clinica.dental_back_spring.repository;

import com.clinica.dental_back_spring.entity.Availability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    List<Availability> findByProfessionalIdAndDate(Long professionalId, LocalDate date);
}

