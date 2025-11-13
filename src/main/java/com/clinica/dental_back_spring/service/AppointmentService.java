package com.clinica.dental_back_spring.service;

import com.clinica.dental_back_spring.dto.*;
import com.clinica.dental_back_spring.entity.*;
import com.clinica.dental_back_spring.enums.AppointmentStatus;
import com.clinica.dental_back_spring.enums.CreatedBy;
import com.clinica.dental_back_spring.enums.StatusAvailability;
import com.clinica.dental_back_spring.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AvailabilityRepository availabilityRepository;
    private final SlotRepository slotRepository;
    private final PatientRepository patientRepository;
    private final ProfessionalRepository professionalRepository;
    private final TreatmentRepository treatmentRepository;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            AvailabilityRepository availabilityRepository,
            SlotRepository slotRepository,
            PatientRepository patientRepository,
            ProfessionalRepository professionalRepository,
            TreatmentRepository treatmentRepository
    ) {
        this.appointmentRepository = appointmentRepository;
        this.availabilityRepository = availabilityRepository;
        this.slotRepository = slotRepository;
        this.patientRepository = patientRepository;
        this.professionalRepository = professionalRepository;
        this.treatmentRepository = treatmentRepository;
    }

    // ==========================================================
    // 🩺 CREAR CITA — versión final correcta
    // ==========================================================
    @Transactional
    public AppointmentDTO createAppointment(CreateAppointmentRequest req) {

        // SLOT (hora)
        Slot slot = slotRepository.findById(req.getSlotId())
                .orElseThrow(() -> new IllegalArgumentException("Slot no encontrado"));

        // AVAILABILITY (fecha + profesional)
        Availability availability = availabilityRepository.findBySlotIdAndProfessionalId(
                req.getSlotId(), req.getProfessionalId()
        ).orElseThrow(() -> new IllegalArgumentException(
                "No existe disponibilidad para ese profesional y slot")
        );

        // Asegurar estado libre
        if (availability.getStatus() != StatusAvailability.LIBRE) {
            throw new IllegalStateException("Ese horario ya está ocupado o no disponible");
        }

        // Evitar citas duplicadas en ese slot (seguridad extra)
        long count = appointmentRepository.countBySlotIdAndStatusNot(
                slot.getId(), AppointmentStatus.CANCELADA
        );
        if (count > 0) {
            throw new IllegalStateException("El slot ya está reservado");
        }

        // Paciente
        Patient patient = patientRepository.findById(req.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));

        // Profesional
        Professional professional = professionalRepository.findById(req.getProfessionalId())
                .orElseThrow(() -> new IllegalArgumentException("Profesional no encontrado"));

        // Tratamiento
        Treatment treatment = treatmentRepository.findById(req.getTreatmentId())
                .orElseThrow(() -> new IllegalArgumentException("Tratamiento no encontrado"));

        // FECHA REAL: availability.date + slot.startTime
        LocalDateTime appointmentDate = LocalDateTime.of(
                availability.getDate(),
                slot.getStartTime()
        );

        // Crear cita
        Appointment ap = Appointment.builder()
                .slot(slot)
                .patient(patient)
                .professional(professional)
                .treatment(treatment)
                .status(AppointmentStatus.PENDIENTE)
                .date(appointmentDate)
                .duration(treatment.getDuration())
                .createdBy(req.getCreatedBy() != null ? req.getCreatedBy() : CreatedBy.ADMIN)
                .build();

        appointmentRepository.save(ap);

        // Marcar availability como RESERVADO
        availability.setStatus(StatusAvailability.RESERVADO);

        return toDTO(ap);
    }

    // ==========================================================
    // 👤 CITAS POR PACIENTE
    // ==========================================================
    public List<AppointmentDTO> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId)
                .stream().map(this::toDTO).toList();
    }

    // ==========================================================
    // 👨‍⚕️ CITAS POR PROFESIONAL
    // ==========================================================
    public List<AppointmentDTO> getAppointmentsByProfessional(Long professionalId) {
        return appointmentRepository.findByProfessionalId(professionalId)
                .stream().map(this::toDTO).toList();
    }

    // ==========================================================
    // 🔁 ACTUALIZAR ESTADO
    // ==========================================================
    @Transactional
    public AppointmentDTO updateStatus(Long id, AppointmentStatus status) {

        Appointment ap = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

        ap.setStatus(status);

        // Si pasa a cancelada → liberar disponibilidad
        if (status == AppointmentStatus.CANCELADA) {
            Availability av = availabilityRepository.findBySlotIdAndProfessionalId(
                    ap.getSlot().getId(),
                    ap.getProfessional().getId()
            ).orElse(null);

            if (av != null) {
                av.setStatus(StatusAvailability.LIBRE);
            }
        }

        return toDTO(ap);
    }

    // ==========================================================
    // ❌ CANCELAR CITA
    // ==========================================================
    @Transactional
    public AppointmentDTO cancelAppointment(Long id, String reason) {

        Appointment ap = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

        ap.setStatus(AppointmentStatus.CANCELADA);
        ap.setCancellationReason(reason);

        Availability av = availabilityRepository.findBySlotIdAndProfessionalId(
                ap.getSlot().getId(),
                ap.getProfessional().getId()
        ).orElse(null);

        if (av != null) {
            av.setStatus(StatusAvailability.LIBRE);
        }

        return toDTO(ap);
    }

    // ==========================================================
    // 🔁 toDTO()
    // ==========================================================
    private AppointmentDTO toDTO(Appointment ap) {
        return AppointmentDTO.builder()
                .id(ap.getId())
                .date(ap.getDate())
                .duration(ap.getDuration())
                .status(ap.getStatus().name())
                .createdBy(ap.getCreatedBy().name().toLowerCase())
                .patient(SimplePatientDTO.from(ap.getPatient()))
                .professional(SimpleProfessionalDTO.from(ap.getProfessional()))
                .treatment(SimpleTreatmentDTO.from(ap.getTreatment()))
                .build();
    }
}
