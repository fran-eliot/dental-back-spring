package com.clinica.dental_back_spring.repository;

import com.clinica.dental_back_spring.entity.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TreatmentRepository extends JpaRepository<Treatment, Long> {
}
