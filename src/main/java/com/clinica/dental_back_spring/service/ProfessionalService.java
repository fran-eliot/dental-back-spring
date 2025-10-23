package com.clinica.dental_back_spring.service;

import com.clinica.dental_back_spring.dto.CreateProfessionalRequest;
import com.clinica.dental_back_spring.dto.ProfessionalDTO;
import com.clinica.dental_back_spring.dto.UpdateProfessionalRequest;
import com.clinica.dental_back_spring.entity.Professional;
import com.clinica.dental_back_spring.repository.ProfessionalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfessionalService {

    private final ProfessionalRepository professionalRepository;

    public ProfessionalService(ProfessionalRepository professionalRepository) {
        this.professionalRepository = professionalRepository;
    }

    private ProfessionalDTO toDto(Professional p) {
        return ProfessionalDTO.builder()
                .id(p.getId())
                .nif(p.getNif())
                .licence(p.getLicence())
                .name(p.getName())
                .lastName(p.getLast_name())
                .phone(p.getPhone())
                .email(p.getEmail())
                .room(p.getRoom())
                .active(p.isActive())
                .build();
    }

    @Transactional(readOnly = true)
    public List<ProfessionalDTO> findAll(String query) {
        List<Professional> list;
        if (StringUtils.hasText(query)) {
            list = professionalRepository.findAll().stream()
                    .filter(p -> (p.getNif() != null && p.getNif().toLowerCase().contains(query.toLowerCase()))
                            || (p.getName() != null && p.getName().toLowerCase().contains(query.toLowerCase()))
                            || (p.getLast_name() != null && p.getLast_name().toLowerCase().contains(query.toLowerCase()))
                            || (p.getEmail() != null && p.getEmail().toLowerCase().contains(query.toLowerCase())))
                    .collect(Collectors.toList());
        } else {
            list = professionalRepository.findAll();
        }
        return list.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProfessionalDTO findById(Long id) {
        return professionalRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Professional not found"));
    }

    @Transactional
    public ProfessionalDTO create(CreateProfessionalRequest req) {
        Professional p = Professional.builder()
                .nif(req.getNif())
                .licence(req.getLicence())
                .name(req.getName())
                .last_name(req.getLastName())
                .phone(req.getPhone())
                .email(req.getEmail())
                .room(req.getRoom())
                .active(true)
                .build();
        Professional saved = professionalRepository.save(p);
        return toDto(saved);
    }

    @Transactional
    public ProfessionalDTO update(Long id, UpdateProfessionalRequest req) {
        Professional p = professionalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Professional not found"));
        if (req.getNif() != null) p.setNif(req.getNif());
        if (req.getLicence() != null) p.setLicence(req.getLicence());
        if (req.getName() != null) p.setName(req.getName());
        if (req.getLastName() != null) p.setLast_name(req.getLastName());
        if (req.getPhone() != null) p.setPhone(req.getPhone());
        if (req.getEmail() != null) p.setEmail(req.getEmail());
        if (req.getRoom() != null) p.setRoom(req.getRoom());
        if (req.getActive() != null) p.setActive(req.getActive());
        Professional saved = professionalRepository.save(p);
        return toDto(saved);
    }

    @Transactional
    public void softDelete(Long id) {
        Professional p = professionalRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Professional not found"));
        p.setActive(false);
        professionalRepository.save(p);
    }
}
