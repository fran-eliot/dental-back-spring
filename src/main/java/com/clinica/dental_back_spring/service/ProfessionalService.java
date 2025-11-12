package com.clinica.dental_back_spring.service;

import com.clinica.dental_back_spring.dto.CreateProfessionalRequest;
import com.clinica.dental_back_spring.dto.ProfessionalDTO;
import com.clinica.dental_back_spring.dto.UpdateProfessionalRequest;
import com.clinica.dental_back_spring.entity.Professional;
import com.clinica.dental_back_spring.repository.ProfessionalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfessionalService {

    private final ProfessionalRepository professionalRepository;

    public ProfessionalService(ProfessionalRepository professionalRepository) {
        this.professionalRepository = professionalRepository;
    }

    // ==========================================================
    // 🔍 LISTAR O BUSCAR PROFESIONALES
    // ==========================================================
    public List<ProfessionalDTO> findAll(String query) {
        List<Professional> professionals = (query == null || query.isBlank())
                ? professionalRepository.findAll()
                : professionalRepository.search(query);

        return professionals.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ==========================================================
    // 🔍 OBTENER PROFESIONAL POR ID
    // ==========================================================
    public ProfessionalDTO findById(Long id) {
        Professional p = professionalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profesional no encontrado"));
        return toDTO(p);
    }

    // ==========================================================
    // ➕ CREAR PROFESIONAL
    // ==========================================================
    @Transactional
    public ProfessionalDTO create(CreateProfessionalRequest req) {
        Professional p = Professional.builder()
                .nif(req.getNif())
                .licence(req.getLicence())
                .name(req.getName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .room(req.getRoom())
                .active(true)
                .build();

        professionalRepository.save(p);
        return toDTO(p);
    }

    // ==========================================================
    // ✏️ ACTUALIZAR PROFESIONAL
    // ==========================================================
    @Transactional
    public ProfessionalDTO update(Long id, UpdateProfessionalRequest req) {
        Professional p = professionalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profesional no encontrado"));

        if (req.getName() != null) p.setName(req.getName());
        if (req.getLastName() != null) p.setLastName(req.getLastName());
        if (req.getEmail() != null) p.setEmail(req.getEmail());
        if (req.getPhone() != null) p.setPhone(req.getPhone());
        if (req.getRoom() != null) p.setRoom(req.getRoom());
        if (req.getLicence() != null) p.setLicence(req.getLicence());

        professionalRepository.save(p);
        return toDTO(p);
    }

    // ==========================================================
    // 🗑️ DESACTIVAR PROFESIONAL (SOFT DELETE)
    // ==========================================================
    @Transactional
    public void softDelete(Long id) {
        Professional p = professionalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profesional no encontrado"));

        p.setActive(false);
        professionalRepository.save(p);
    }

    // ==========================================================
    // 🔁 Conversión a DTO
    // ==========================================================
    private ProfessionalDTO toDTO(Professional p) {
        return ProfessionalDTO.builder()
                .id(p.getId())
                .nif(p.getNif())
                .licence(p.getLicence())
                .name(p.getName())
                .lastName(p.getLastName())
                .email(p.getEmail())
                .phone(p.getPhone())
                .room(p.getRoom())
                .active(p.isActive())
                .build();
    }
}
