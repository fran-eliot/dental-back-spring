package com.clinica.dental_back_spring.service;

import com.clinica.dental_back_spring.dto.CreatePatientRequest;
import com.clinica.dental_back_spring.dto.PatientDTO;
import com.clinica.dental_back_spring.dto.UpdatePatientRequest;
import com.clinica.dental_back_spring.entity.Patient;
import com.clinica.dental_back_spring.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    private PatientDTO toDto(Patient p) {
        return PatientDTO.builder()
                .id(p.getId())
                .nif(p.getNif())
                .firstName(p.getFirstName())
                .lastName(p.getLastName())
                .email(p.getEmail())
                .phone(p.getPhone())
                .active(p.isActive())
                .build();
    }

    @Transactional(readOnly = true)
    public List<PatientDTO> findAll(String query) {
        List<Patient> list;
        if (StringUtils.hasText(query)) {
            // Buscar por nif, nombre o email (puedes ajustar el repositorio si quieres consultas más eficientes)
            list = patientRepository.findAll().stream()
                    .filter(p -> (p.getNif() != null && p.getNif().toLowerCase().contains(query.toLowerCase()))
                            || (p.getFirstName() != null && p.getFirstName().toLowerCase().contains(query.toLowerCase()))
                            || (p.getLastName() != null && p.getLastName().toLowerCase().contains(query.toLowerCase()))
                            || (p.getEmail() != null && p.getEmail().toLowerCase().contains(query.toLowerCase())))
                    .collect(Collectors.toList());
        } else {
            list = patientRepository.findAll();
        }
        return list.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PatientDTO findById(Long id) {
        return patientRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
    }

    @Transactional
    public PatientDTO create(CreatePatientRequest req) {
        Patient p = Patient.builder()
                .nif(req.getNif())
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .active(true)
                .build();
        Patient saved = patientRepository.save(p);
        return toDto(saved);
    }

    @Transactional
    public PatientDTO update(Long id, UpdatePatientRequest req) {
        Patient p = patientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        if (req.getNif() != null) p.setNif(req.getNif());
        if (req.getFirstName() != null) p.setFirstName(req.getFirstName());
        if (req.getLastName() != null) p.setLastName(req.getLastName());
        if (req.getEmail() != null) p.setEmail(req.getEmail());
        if (req.getPhone() != null) p.setPhone(req.getPhone());
        if (req.getActive() != null) p.setActive(req.getActive());
        Patient saved = patientRepository.save(p);
        return toDto(saved);
    }

    @Transactional
    public void softDelete(Long id) {
        Patient p = patientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        p.setActive(false);
        patientRepository.save(p);
    }
}

