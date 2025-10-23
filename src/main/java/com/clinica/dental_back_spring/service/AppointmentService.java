package com.clinica.dental_back_spring.service;

import com.clinica.dental_back_spring.dto.CreateAppointmentRequest;
import com.clinica.dental_back_spring.entity.Appointment;
import com.clinica.dental_back_spring.entity.Patient;
import com.clinica.dental_back_spring.entity.Slot;
import com.clinica.dental_back_spring.entity.Treatment;
import com.clinica.dental_back_spring.enums.AppointmentStatus;
import com.clinica.dental_back_spring.enums.CreatedBy;
import com.clinica.dental_back_spring.repository.AppointmentRepository;
import com.clinica.dental_back_spring.repository.PatientRepository;
import com.clinica.dental_back_spring.repository.SlotRepository;
import com.clinica.dental_back_spring.repository.TreatmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AppointmentService {

    private final SlotRepository slotRepository;
    private final PatientRepository patientRepository;
    private final TreatmentRepository treatmentRepository;
    private final AppointmentRepository appointmentRepository;

    public AppointmentService(SlotRepository slotRepository,
                              PatientRepository patientRepository,
                              TreatmentRepository treatmentRepository,
                              AppointmentRepository appointmentRepository) {
        this.slotRepository = slotRepository;
        this.patientRepository = patientRepository;
        this.treatmentRepository = treatmentRepository;
        this.appointmentRepository = appointmentRepository;
    }

    /**
     * Crea una cita asegurando que no haya otra cita activa en el slot.
     * Usa bloqueo pesimista sobre el slot para evitar race conditions.
     */
    @Transactional
    public Appointment createAppointment(CreateAppointmentRequest req) {
        // 1. Cargar slot con lock
        Slot slot = slotRepository.findByIdForUpdate(req.getSlotId())
                .orElseThrow(() -> new IllegalArgumentException("Slot not found"));

        // 2. Comprobar que no haya citas activas (no CANCELADAS) para ese slot
        long activeCount = appointmentRepository.countBySlotIdAndStatusNot(slot.getId(), AppointmentStatus.CANCELADA);
        if (activeCount > 0) {
            throw new IllegalStateException("Slot already booked");
        }

        // 3. Obtener patient y treatment
        Patient patient = patientRepository.findById(req.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        Treatment treatment = treatmentRepository.findById(req.getTreatmentId())
                .orElseThrow(() -> new IllegalArgumentException("Treatment not found"));

        // 4. Crear appointment
        Appointment ap = Appointment.builder()
                .slot(slot)
                .patient(patient)
                .professional(null) // opcional: si quieres buscar professional por id, acoplarlo
                .treatment(treatment)
                .status(AppointmentStatus.PENDIENTE)
                .date(LocalDateTime.now())
                .duration(treatment.getDuration() != null ? treatment.getDuration() : 30)
                .createdBy(CreatedBy.PATIENT) // o inferir del token
                .build();

        Appointment saved = appointmentRepository.save(ap);

        return saved;
    }
}

