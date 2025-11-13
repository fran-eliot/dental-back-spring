package com.clinica.dental_back_spring.service;

import com.clinica.dental_back_spring.dto.CreateProfessionalRequest;
import com.clinica.dental_back_spring.dto.ProfessionalDTO;
import com.clinica.dental_back_spring.dto.UpdateProfessionalRequest;
import com.clinica.dental_back_spring.entity.Professional;
import com.clinica.dental_back_spring.entity.User;
import com.clinica.dental_back_spring.enums.Role;
import com.clinica.dental_back_spring.repository.ProfessionalRepository;
import com.clinica.dental_back_spring.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfessionalService {

    private final ProfessionalRepository professionalRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfessionalService(ProfessionalRepository professionalRepository,
                               UserRepository userRepository,
                               PasswordEncoder passwordEncoder) {
        this.professionalRepository = professionalRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ==========================================================
    // 🔍 LISTAR / BUSCAR
    // ==========================================================
    public List<ProfessionalDTO> findAll(String query) {
        List<Professional> professionals =
                (query == null || query.isBlank())
                        ? professionalRepository.findAll()
                        : professionalRepository.search(query);

        return professionals.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ==========================================================
    // 🔍 OBTENER POR ID
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

        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // Crear usuario asociado
        User user = User.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode("123456"))
                .role(Role.ROLE_DENTISTA)
                .active(true)
                .build();
        userRepository.save(user);

        // Crear profesional
        Professional p = Professional.builder()
                .nif(req.getNif())
                .license(req.getLicense())
                .name(req.getName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .room(req.getRoom())
                .active(true)
                .user(user)
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

        // EMAIL: validación + update en Professional + User
        if (req.getEmail() != null && !req.getEmail().equals(p.getEmail())) {

            if (userRepository.existsByEmail(req.getEmail())) {
                throw new IllegalArgumentException("Ese email ya está en uso por otro usuario");
            }

            p.setEmail(req.getEmail());
            if (p.getUser() != null) {
                p.getUser().setEmail(req.getEmail());
            }
        }

        if (req.getName() != null) p.setName(req.getName());
        if (req.getLastName() != null) p.setLastName(req.getLastName());
        if (req.getPhone() != null) p.setPhone(req.getPhone());
        if (req.getNif() != null) p.setNif(req.getNif());
        if (req.getRoom() != null) p.setRoom(req.getRoom());
        if (req.getLicense() != null) p.setLicense(req.getLicense());

        return toDTO(p);
    }

    // ==========================================================
    // 🗑️ SOFT DELETE
    // ==========================================================
    @Transactional
    public void softDelete(Long id) {

        Professional p = professionalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profesional no encontrado"));

        p.setActive(false);

        // Desactivar también el usuario
        if (p.getUser() != null) {
            p.getUser().setActive(false);
        }
    }

    // ==========================================================
    // 🔁 DTO
    // ==========================================================
    private ProfessionalDTO toDTO(Professional p) {
        return ProfessionalDTO.builder()
                .id(p.getId())
                .nif(p.getNif())
                .license(p.getLicense())
                .name(p.getName())
                .lastName(p.getLastName())
                .email(p.getEmail())
                .phone(p.getPhone())
                .room(p.getRoom())
                .active(p.isActive())
                .userId(p.getUser() != null ? p.getUser().getId() : null)
                .build();
    }
}

