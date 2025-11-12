package com.clinica.dental_back_spring.repository;

import com.clinica.dental_back_spring.entity.Professional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfessionalRepository extends JpaRepository<Professional, Long> {

    @Query("SELECT p FROM Professional p WHERE " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(p.licence) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Professional> search(@Param("query") String query);
}


