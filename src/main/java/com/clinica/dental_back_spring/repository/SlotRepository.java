package com.clinica.dental_back_spring.repository;

import com.clinica.dental_back_spring.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import jakarta.persistence.LockModeType;
import java.util.Optional;

public interface SlotRepository extends JpaRepository<Slot, Long> {

    // Método para adquirir lock pesimista sobre el slot
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Slot s where s.id = :id")
    Optional<Slot> findByIdForUpdate(Long id);
}
