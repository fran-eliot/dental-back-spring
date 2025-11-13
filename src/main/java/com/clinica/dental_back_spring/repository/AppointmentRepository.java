package com.clinica.dental_back_spring.repository;

import com.clinica.dental_back_spring.entity.Appointment;
import com.clinica.dental_back_spring.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByProfessionalId(Long professionalId);

    List<Appointment> findByProfessionalIdAndStatusNot(Long professionalId, AppointmentStatus status);

    List<Appointment> findByProfessionalIdAndDateBetween(
            Long professionalId,
            LocalDateTime from,
            LocalDateTime to
    );

    long countBySlotIdAndStatusNot(Long slotId, AppointmentStatus excludedStatus);
}


