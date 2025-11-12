package com.clinica.dental_back_spring.service;

import com.clinica.dental_back_spring.dto.CreateAppointmentRequest;
import com.clinica.dental_back_spring.entity.*;
import com.clinica.dental_back_spring.enums.AppointmentStatus;
import com.clinica.dental_back_spring.enums.CreatedBy;
import com.clinica.dental_back_spring.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final SlotRepository slotRepository;
    private final PatientRepository patientRepository;
    private final ProfessionalRepository professionalRepository;
    private final TreatmentRepository treatmentRepository;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            SlotRepository slotRepository,
            PatientRepository patientRepository,
            ProfessionalRepository professionalRepository,
            TreatmentRepository treatmentRepository
    ) {
        this.appointmentRepository = appointmentRepository;
        this.slotRepository = slotRepository;
        this.patientRepository = patientRepository;
        this.professionalRepository = professionalRepository;
        this.treatmentRepository = treatmentRepository;
    }

    // ==========================================================
    // 🩺 CREAR CITA
    // ==========================================================
    @Transactional
    public Appointment createAppointment(CreateAppointmentRequest req) {
        Slot slot = slotRepository.findById(req.getSlotId())
                .orElseThrow(() -> new IllegalArgumentException("Slot no encontrado"));

        long activeCount = appointmentRepository.countBySlotIdAndStatusNot(slot.getId(), AppointmentStatus.CANCELADA);
        if (activeCount > 0) {
            throw new IllegalStateException("El slot ya está reservado");
        }

        Patient patient = patientRepository.findById(req.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));

        Professional professional = professionalRepository.findById(req.getProfessionalId())
                .orElseThrow(() -> new IllegalArgumentException("Profesional no encontrado"));

        Treatment treatment = treatmentRepository.findById(req.getTreatmentId())
                .orElseThrow(() -> new IllegalArgumentException("Tratamiento no encontrado"));

        Appointment ap = Appointment.builder()
                .slot(slot)
                .patient(patient)
                .professional(professional)
                .treatment(treatment)
                .status(AppointmentStatus.PENDIENTE)
                .date(LocalDateTime.now())
                .duration(treatment.getDuration() != null ? treatment.getDuration() : 30)
                .createdBy(parseCreatedBy(req.getCreatedBy()))
                .build();

        return appointmentRepository.save(ap);
    }

    // ==========================================================
    // 👤 CITAS POR PACIENTE
    // ==========================================================
    public List<Appointment> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    // ==========================================================
    // 👨‍⚕️ CITAS POR PROFESIONAL
    // ==========================================================
    public List<Appointment> getAppointmentsByProfessional(Long professionalId) {
        return appointmentRepository.findByProfessionalId(professionalId);
    }

    // ==========================================================
    // 🔁 ACTUALIZAR ESTADO
    // ==========================================================
    @Transactional
    public Appointment updateStatus(Long appointmentId, AppointmentStatus newStatus) {
        Appointment ap = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

        ap.setStatus(newStatus);
        return appointmentRepository.save(ap);
    }

    // ==========================================================
    // ❌ CANCELAR CITA
    // ==========================================================
    @Transactional
    public Appointment cancelAppointment(Long appointmentId, String reason) {
        Appointment ap = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

        ap.setStatus(AppointmentStatus.CANCELADA);
        ap.setCancellationReason(reason);
        return appointmentRepository.save(ap);
    }

    // ==========================================================
    // 🧠 Auxiliar - Parseo de CreatedBy
    // ==========================================================
    private CreatedBy parseCreatedBy(String createdBy) {
        if (createdBy == null) return CreatedBy.ADMIN;
        try {
            return CreatedBy.valueOf(createdBy.toUpperCase());
        } catch (IllegalArgumentException e) {
            return CreatedBy.ADMIN;
        }
    }
}


