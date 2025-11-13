package com.clinica.dental_back_spring.service;

import com.clinica.dental_back_spring.dto.UserDTO;
import com.clinica.dental_back_spring.entity.User;
import com.clinica.dental_back_spring.enums.Role;
import com.clinica.dental_back_spring.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ==========================================================
    // 🔍 LISTAR TODOS LOS USUARIOS
    // ==========================================================
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ==========================================================
    // 🔍 OBTENER USUARIO POR ID
    // ==========================================================
    public UserDTO findById(Long id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return toDTO(u);
    }

    // ==========================================================
    // 🔄 CAMBIAR ROL
    // ==========================================================
    @Transactional
    public void updateRole(Long id, String newRole) {

        User u = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (newRole == null || newRole.isBlank()) {
            throw new IllegalArgumentException("El rol no puede estar vacío");
        }

        try {
            Role roleEnum = Role.valueOf(newRole.toUpperCase());
            u.setRole(roleEnum);
            userRepository.save(u);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Rol no válido: " + newRole);
        }
    }

    // ==========================================================
    // 🔑 CAMBIAR CONTRASEÑA
    // ==========================================================
    @Transactional
    public void updatePassword(Long id, String newPassword) {

        User u = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("La nueva contraseña no puede estar vacía");
        }

        u.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(u);
    }

    // ==========================================================
    // 🚫 DESACTIVAR USUARIO
    // ==========================================================
    @Transactional
    public void deactivate(Long id) {

        User u = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        u.setActive(false);
        userRepository.save(u);
    }

    // ==========================================================
    // 🧩 Conversión a DTO
    // ==========================================================
    private UserDTO toDTO(User u) {
        return UserDTO.builder()
                .id(u.getId())
                .email(u.getEmail())
                .role(u.getRole().name())   // ROLE_ADMIN, ROLE_DENTISTA, ROLE_PACIENTE
                .active(u.isActive())
                .build();
    }
}
