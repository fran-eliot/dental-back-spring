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
import java.util.stream.Collectors;

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
    // 📅 LISTAR DISPONIBILIDADES
    // ==========================================================
    public List<AvailabilityDTO> findAll() {
        return availabilityRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<AvailabilityDTO> findByProfessionalAndDate(Long professionalId, LocalDate date) {
        return availabilityRepository.findByProfessionalIdAndDate(professionalId, date)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ==========================================================
    // 🔍 OBTENER UNA DISPONIBILIDAD
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

        Availability a = Availability.builder()
                .professional(professional)
                .slot(slot)
                .date(req.getDate())
                .status(StatusAvailability.LIBRE)
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

        if (req.getStatus() != null) a.setStatus(req.getStatus());
        if (req.getDate() != null) a.setDate(req.getDate());

        availabilityRepository.save(a);
        return toDTO(a);
    }

    // ==========================================================
    // 🚫 ELIMINAR (SOFT DELETE)
    // ==========================================================
    @Transactional
    public void softDelete(Long id) {
        Availability a = availabilityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Disponibilidad no encontrada"));

        a.setStatus(StatusAvailability.NO_DISPONIBLE);
        availabilityRepository.save(a);
    }

    // ==========================================================
    // 🔁 Conversor a DTO
    // ==========================================================
    private AvailabilityDTO toDTO(Availability a) {
        return AvailabilityDTO.builder()
                .id(a.getId())
                .date(a.getDate())
                .status(a.getStatus())
                .professionalId(a.getProfessional().getId())
                .slotId(a.getSlot().getId())
                .build();
    }
}
