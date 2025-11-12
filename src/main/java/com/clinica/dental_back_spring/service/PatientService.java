package com.clinica.dental_back_spring.service;

import com.clinica.dental_back_spring.dto.CreatePatientRequest;
import com.clinica.dental_back_spring.dto.PatientDTO;
import com.clinica.dental_back_spring.dto.UpdatePatientRequest;
import com.clinica.dental_back_spring.entity.Patient;
import com.clinica.dental_back_spring.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    // ==========================================================
    // 🔍 LISTAR O BUSCAR PACIENTES
    // ==========================================================
    public List<PatientDTO> findAll(String query) {
        List<Patient> patients = (query == null || query.isBlank())
                ? patientRepository.findAll()
                : patientRepository.search(query);

        return patients.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ==========================================================
    // 🔍 OBTENER PACIENTE POR ID
    // ==========================================================
    public PatientDTO findById(Long id) {
        Patient p = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));
        return toDTO(p);
    }

    // ==========================================================
    // ➕ CREAR PACIENTE
    // ==========================================================
    @Transactional
    public PatientDTO create(CreatePatientRequest req) {
        Patient patient = Patient.builder()
                .nif(req.getNif())
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .active(true)
                .build();

        patientRepository.save(patient);
        return toDTO(patient);
    }

    // ==========================================================
    // ✏️ ACTUALIZAR PACIENTE
    // ==========================================================
    @Transactional
    public PatientDTO update(Long id, UpdatePatientRequest req) {
        Patient p = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));

        if (req.getFirstName() != null) p.setFirstName(req.getFirstName());
        if (req.getLastName() != null) p.setLastName(req.getLastName());
        if (req.getEmail() != null) p.setEmail(req.getEmail());
        if (req.getPhone() != null) p.setPhone(req.getPhone());
        if (req.getNif() != null) p.setNif(req.getNif());

        patientRepository.save(p);
        return toDTO(p);
    }

    // ==========================================================
    // 🗑️ DESACTIVAR PACIENTE (SOFT DELETE)
    // ==========================================================
    @Transactional
    public void softDelete(Long id) {
        Patient p = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));

        p.setActive(false);
        patientRepository.save(p);
    }

    // ==========================================================
    // 🔁 Conversión a DTO
    // ==========================================================
    private PatientDTO toDTO(Patient p) {
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
}
