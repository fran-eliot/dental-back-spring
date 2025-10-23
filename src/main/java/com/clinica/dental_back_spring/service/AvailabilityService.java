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

    private AvailabilityDTO toDto(Availability a) {
        return AvailabilityDTO.builder()
                .id(a.getId())
                .professionalId(a.getProfessional() != null ? a.getProfessional().getId() : null)
                .date(a.getDate())
                .status(a.getStatus())
                .slotId(a.getSlot() != null ? a.getSlot().getId() : null)
                .build();
    }

    @Transactional(readOnly = true)
    public List<AvailabilityDTO> findByProfessionalAndDate(Long professionalId, java.time.LocalDate date) {
        List<Availability> list = availabilityRepository.findByProfessionalIdAndDate(professionalId, date);
        return list.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AvailabilityDTO> findAll() {
        return availabilityRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AvailabilityDTO findById(Long id) {
        return availabilityRepository.findById(id).map(this::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Availability not found"));
    }

    @Transactional
    public AvailabilityDTO create(CreateAvailabilityRequest req) {
        Professional prof = professionalRepository.findById(req.getProfessionalId())
                .orElseThrow(() -> new IllegalArgumentException("Professional not found"));

        Slot slot = null;
        if (req.getSlotId() != null) {
            slot = slotRepository.findById(req.getSlotId())
                    .orElseThrow(() -> new IllegalArgumentException("Slot not found"));
        }

        Availability a = Availability.builder()
                .professional(prof)
                .date(req.getDate())
                .status(req.getStatus())
                .slot(slot)
                .build();

        Availability saved = availabilityRepository.save(a);
        return toDto(saved);
    }

    @Transactional
    public AvailabilityDTO update(Long id, UpdateAvailabilityRequest req) {
        Availability a = availabilityRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Availability not found"));

        if (req.getProfessionalId() != null) {
            Professional prof = professionalRepository.findById(req.getProfessionalId())
                    .orElseThrow(() -> new IllegalArgumentException("Professional not found"));
            a.setProfessional(prof);
        }
        if (req.getDate() != null) a.setDate(req.getDate());
        if (req.getStatus() != null) a.setStatus(req.getStatus());
        if (req.getSlotId() != null) {
            Slot slot = slotRepository.findById(req.getSlotId())
                    .orElseThrow(() -> new IllegalArgumentException("Slot not found"));
            a.setSlot(slot);
        }
        Availability saved = availabilityRepository.save(a);
        return toDto(saved);
    }

    @Transactional
    public void softDelete(Long id) {
        Availability a = availabilityRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Availability not found"));
        a.setStatus(StatusAvailability.NO_DISPONIBLE);
        availabilityRepository.save(a);
    }
}

