package com.clinica.dental_back_spring.repository;

import com.clinica.dental_back_spring.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
