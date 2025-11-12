package com.clinica.dental_back_spring.service;

import com.clinica.dental_back_spring.dto.CreateTreatmentRequest;
import com.clinica.dental_back_spring.dto.TreatmentDTO;
import com.clinica.dental_back_spring.dto.UpdateTreatmentRequest;
import com.clinica.dental_back_spring.entity.Treatment;
import com.clinica.dental_back_spring.repository.TreatmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TreatmentService {

    private final TreatmentRepository treatmentRepository;

    public TreatmentService(TreatmentRepository treatmentRepository) {
        this.treatmentRepository = treatmentRepository;
    }

    // ==========================================================
    // 🔍 LISTAR O FILTRAR TRATAMIENTOS
    // ==========================================================
    public List<TreatmentDTO> findAll(String query, Boolean visibleOnly) {
        List<Treatment> list = treatmentRepository.findFiltered(query, visibleOnly);
        return list.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ==========================================================
    // 🔍 OBTENER TRATAMIENTO POR ID
    // ==========================================================
    public TreatmentDTO findById(Long id) {
        Treatment t = treatmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tratamiento no encontrado"));
        return toDTO(t);
    }

    // ==========================================================
    // ➕ CREAR TRATAMIENTO
    // ==========================================================
    @Transactional
    public TreatmentDTO create(CreateTreatmentRequest req) {
        Treatment t = Treatment.builder()
                .name(req.getName())
                .type(req.getType())
                .duration(req.getDuration())
                .price(req.getPrice())
                .visible(req.getVisible())
                .build();

        treatmentRepository.save(t);
        return toDTO(t);
    }

    // ==========================================================
    // ✏️ ACTUALIZAR TRATAMIENTO
    // ==========================================================
    @Transactional
    public TreatmentDTO update(Long id, UpdateTreatmentRequest req) {
        Treatment t = treatmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tratamiento no encontrado"));

        if (req.getName() != null) t.setName(req.getName());
        if (req.getType() != null) t.setType(req.getType());
        if (req.getDuration() != null) t.setDuration(req.getDuration());
        if (req.getPrice() != null) t.setPrice(req.getPrice());
        if (req.getVisible() != null) t.setVisible(req.getVisible());

        treatmentRepository.save(t);
        return toDTO(t);
    }

    // ==========================================================
    // 🚫 DESACTIVAR TRATAMIENTO (SOFT DELETE)
    // ==========================================================
    @Transactional
    public void softDelete(Long id) {
        Treatment t = treatmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tratamiento no encontrado"));

        t.setVisible(false);
        treatmentRepository.save(t);
    }

    // ==========================================================
    // 🔁 Conversión a DTO
    // ==========================================================
    private TreatmentDTO toDTO(Treatment t) {
        return TreatmentDTO.builder()
                .id(t.getId())
                .name(t.getName())
                .type(t.getType())
                .duration(t.getDuration())
                .price(t.getPrice())
                .visible(t.isVisible())
                .build();
    }
}
