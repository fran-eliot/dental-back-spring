package com.clinica.dental_back_spring.service;

import com.clinica.dental_back_spring.dto.AvailabilityDTO;
import com.clinica.dental_back_spring.dto.CreateAvailabilityRequest;
import com.clinica.dental_back_spring.dto.UpdateAvailabilityRequest;
import com.clinica.dental_back_spring.entity.Availability;
import com.clinica.dental_back_spring.entity.Professional;
import com.clinica.dental_back_spring.entity.Slot;
import com.clinica.dental_back_spring.enums.StatusAvailability;
import com.clinica.dental_back_spring.repository.AvailabilityRepository;
import com.clinica.dental_back_spring.repository.ProfessionalRepository;
import com.clinica.dental_back_spring.repository.SlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final ProfessionalRepository professionalRepository;
    private final SlotRepository slotRepository;

    public AvailabilityService(AvailabilityRepository availabilityRepository,
                               ProfessionalRepository professionalRepository,
                               SlotRepository slotRepository) {
        this.availabilityRepository = availabilityRepository;
        this.professionalRepository = professionalRepository;
        this.slotRepository = slotRepository;
    }

    // ==========================================================
    // 📅 LISTAR
    // ==========================================================
    public List<AvailabilityDTO> findAll() {
        return availabilityRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public List<AvailabilityDTO> findByProfessionalAndDate(Long professionalId, LocalDate date) {
        return availabilityRepository.findByProfessionalIdAndDate(professionalId, date)
                .stream().map(this::toDTO)
                .toList();
    }

    // ==========================================================
    // 🔍 OBTENER
    // ==========================================================
    public AvailabilityDTO findById(Long id) {
        Availability a = availabilityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Disponibilidad no encontrada"));
        return toDTO(a);
    }

    // ==========================================================
    // ➕ CREAR DISPONIBILIDAD
    // ==========================================================
    @Transactional
    public AvailabilityDTO create(CreateAvailabilityRequest req) {

        Professional professional = professionalRepository.findById(req.getProfessionalId())
                .orElseThrow(() -> new IllegalArgumentException("Profesional no encontrado"));

        Slot slot = slotRepository.findById(req.getSlotId())
                .orElseThrow(() -> new IllegalArgumentException("Slot no encontrado"));

        // 🚫 Evitar duplicados (profesional + día + slot)
        if (availabilityRepository.existsByProfessionalIdAndDateAndSlotId(
                req.getProfessionalId(), req.getDate(), req.getSlotId()
        )) {
            throw new IllegalArgumentException("Ya existe disponibilidad para ese profesional en ese día y hora.");
        }

        Availability a = Availability.builder()
                .professional(professional)
                .slot(slot)
                .date(req.getDate())
                .status(StatusAvailability.LIBRE) // siempre libre al crear
                .build();

        availabilityRepository.save(a);

        return toDTO(a);
    }

    // ==========================================================
    // ✏️ ACTUALIZAR DISPONIBILIDAD
    // ==========================================================
    @Transactional
    public AvailabilityDTO update(Long id, UpdateAvailabilityRequest req) {
        Availability a = availabilityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Disponibilidad no encontrada"));

        // =============================
        // ❗ Cambiar fecha (solo si NO tiene citas)
        // =============================
        if (req.getDate() != null && !req.getDate().equals(a.getDate())) {

            if (!a.getSlot().getAppointments().isEmpty()) {
                throw new IllegalArgumentException("No se puede cambiar la fecha porque esta disponibilidad tiene citas asociadas.");
            }

            // Evitar duplicados con nueva fecha
            if (availabilityRepository.existsByProfessionalIdAndDateAndSlotId(
                    a.getProfessional().getId(), req.getDate(), a.getSlot().getId()
            )) {
                throw new IllegalArgumentException("Ya existe una disponibilidad para ese profesional en la fecha nueva.");
            }

            a.setDate(req.getDate());
        }

        // =============================
        // Cambiar estado (LIBRE, OCUPADO, NO_DISPONIBLE)
        // =============================
        if (req.getStatus() != null) {
            a.setStatus(req.getStatus());
        }

        return toDTO(a);
    }

    // ==========================================================
    // 🚫 ELIMINAR (SOFT DELETE)
    // ==========================================================
    @Transactional
    public void softDelete(Long id) {
        Availability a = availabilityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Disponibilidad no encontrada"));

        // 🚫 No eliminar si tiene citas asociadas
        if (!a.getSlot().getAppointments().isEmpty()) {
            throw new IllegalArgumentException("No se puede eliminar esta disponibilidad porque tiene citas asociadas.");
        }

        a.setStatus(StatusAvailability.NO_DISPONIBLE);
    }

    // ==========================================================
    // 🔁 DTO
    // ==========================================================
    private AvailabilityDTO toDTO(Availability a) {
        return AvailabilityDTO.builder()
                .id(a.getId())
                .professionalId(a.getProfessional().getId())
                .date(a.getDate())
                .status(a.getStatus())
                .slotId(a.getSlot().getId())
                .startTime(a.getSlot().getStartTime())
                .endTime(a.getSlot().getEndTime())
                .period(a.getSlot().getPeriod())
                .build();
    }
}

