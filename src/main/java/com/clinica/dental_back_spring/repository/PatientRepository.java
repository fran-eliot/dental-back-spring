package com.clinica.dental_back_spring.repository;

import com.clinica.dental_back_spring.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query("SELECT p FROM Patient p WHERE " +
            "LOWER(p.firstName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(p.nif) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Patient> search(@Param("query") String query);
}


