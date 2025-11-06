package com.clinica.dental_back_spring.repository;

import com.clinica.dental_back_spring.entity.Appointment;
import com.clinica.dental_back_spring.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByProfessionalId(Long professionalId);

    List<Appointment> findByProfessionalIdAndDateBetween(Long professionalId, java.time.LocalDateTime from, java.time.LocalDateTime to);

    // Cuenta citas activas/no canceladas para un slot
    long countBySlotIdAndStatusNot(Long slotId, AppointmentStatus excludedStatus);
}

