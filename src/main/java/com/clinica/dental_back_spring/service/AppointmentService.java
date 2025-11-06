package com.clinica.dental_back_spring.service;

import com.clinica.dental_back_spring.entity.Appointment;
import com.clinica.dental_back_spring.entity.Patient;
import com.clinica.dental_back_spring.entity.*;
import com.clinica.dental_back_spring.repository.*;
import com.clinica.dental_back_spring.repository.AppointmentRepository;
import com.clinica.dental_back_spring.repository.PatientRepository;
import com.clinica.dental_back_spring.dto.CreateAppointmentRequest;
import com.clinica.dental_back_spring.enums.AppointmentStatus;
import com.clinica.dental_back_spring.enums.CreatedBy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    private final SlotRepository slotRepository;
    private final PatientRepository patientRepository;
    private final TreatmentRepository treatmentRepository;
    private final ProfessionalRepository professionalRepository;
    private final AppointmentRepository appointmentRepository;

    public AppointmentService(
            SlotRepository slotRepository,
            PatientRepository patientRepository,
            ProfessionalRepository professionalRepository,
            TreatmentRepository treatmentRepository,
            AppointmentRepository appointmentRepository
    ) {
        this.slotRepository = slotRepository;
        this.patientRepository = patientRepository;
        this.professionalRepository = professionalRepository;
        this.treatmentRepository = treatmentRepository;
        this.appointmentRepository = appointmentRepository;
    }

    // ==========================================================
    // 🩺 Crear una cita
    // ==========================================================
    @Transactional
    public Appointment createAppointment(CreateAppointmentRequest req) {

        Slot slot = slotRepository.findById(req.getSlotId())
                .orElseThrow(() -> new IllegalArgumentException("Slot not found"));

        long activeCount = appointmentRepository.countBySlotIdAndStatusNot(slot.getId(), AppointmentStatus.CANCELADA);
        if (activeCount > 0) {
            throw new IllegalStateException("Slot already booked");
        }

        Patient patient = patientRepository.findById(req.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        Professional professional = professionalRepository.findById(req.getProfessionalId())
                .orElseThrow(() -> new IllegalArgumentException("Professional not found"));

        Treatment treatment = treatmentRepository.findById(req.getTreatmentId())
                .orElseThrow(() -> new IllegalArgumentException("Treatment not found"));

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
    // 👤 Obtener citas de un paciente
    // ==========================================================
    public List<Appointment> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    // ==========================================================
    // 👨‍⚕️ Obtener citas de un profesional
    // ==========================================================
    public List<Appointment> getAppointmentsByProfessional(Long professionalId) {
        return appointmentRepository.findByProfessionalId(professionalId);
    }

    // ==========================================================
    // 🔁 Actualizar estado de cita
    // ==========================================================
    @Transactional
    public Appointment updateStatus(Long appointmentId, AppointmentStatus newStatus) {
        Appointment ap = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        ap.setStatus(newStatus);
        return appointmentRepository.save(ap);
    }

    // ==========================================================
    // ❌ Cancelar cita
    // ==========================================================
    @Transactional
    public Appointment cancelAppointment(Long appointmentId, String reason) {
        Appointment ap = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        ap.setStatus(AppointmentStatus.CANCELADA);
        ap.setNotes(reason);
        return appointmentRepository.save(ap);
    }

    // ==========================================================
    // 🔍 Auxiliar para convertir el campo createdBy
    // ==========================================================
    private CreatedBy parseCreatedBy(String createdBy) {
        if (createdBy == null) return CreatedBy.PATIENT;
        try {
            return CreatedBy.valueOf(createdBy.toUpperCase());
        } catch (IllegalArgumentException e) {
            return CreatedBy.PATIENT;
        }
    }
}

