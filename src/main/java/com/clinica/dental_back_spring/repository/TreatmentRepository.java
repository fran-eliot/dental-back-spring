package com.clinica.dental_back_spring.repository;

import com.clinica.dental_back_spring.entity.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, Long> {

    @Query("SELECT t FROM Treatment t WHERE " +
            "(:query IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "AND (:visibleOnly IS NULL OR t.visible = :visibleOnly)")
    List<Treatment> findFiltered(@Param("query") String query, @Param("visibleOnly") Boolean visibleOnly);
}

