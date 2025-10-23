package com.clinica.dental_back_spring.service;

import com.clinica.dental_back_spring.dto.CreateTreatmentRequest;
import com.clinica.dental_back_spring.dto.TreatmentDTO;
import com.clinica.dental_back_spring.dto.UpdateTreatmentRequest;
import com.clinica.dental_back_spring.entity.Treatment;
import com.clinica.dental_back_spring.repository.TreatmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TreatmentService {

    private final TreatmentRepository treatmentRepository;

    public TreatmentService(TreatmentRepository treatmentRepository) {
        this.treatmentRepository = treatmentRepository;
    }

    private TreatmentDTO toDto(Treatment t) {
        return TreatmentDTO.builder()
                .id(t.getId())
                .name(t.getName())
                .type(t.getType())
                .duration(t.getDuration())
                .price(t.getPrice())
                .visible(t.isVisible())
                .build();
    }

    @Transactional(readOnly = true)
    public List<TreatmentDTO> findAll(String query, Boolean visibleOnly) {
        List<Treatment> list = treatmentRepository.findAll();
        return list.stream()
                .filter(t -> {
                    boolean ok = true;
                    if (visibleOnly != null && visibleOnly) ok = t.isVisible();
                    if (StringUtils.hasText(query)) {
                        String q = query.toLowerCase();
                        ok = ok && (t.getName() != null && t.getName().toLowerCase().contains(q)
                                || (t.getType() != null && t.getType().toLowerCase().contains(q)));
                    }
                    return ok;
                })
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TreatmentDTO findById(Long id) {
        return treatmentRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Treatment not found"));
    }

    @Transactional
    public TreatmentDTO create(CreateTreatmentRequest req) {
        Treatment t = Treatment.builder()
                .name(req.getName())
                .type(req.getType())
                .duration(req.getDuration())
                .price(req.getPrice())
                .visible(req.getVisible() != null ? req.getVisible() : true)
                .build();
        Treatment saved = treatmentRepository.save(t);
        return toDto(saved);
    }

    @Transactional
    public TreatmentDTO update(Long id, UpdateTreatmentRequest req) {
        Treatment t = treatmentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Treatment not found"));
        if (req.getName() != null) t.setName(req.getName());
        if (req.getType() != null) t.setType(req.getType());
        if (req.getDuration() != null) t.setDuration(req.getDuration());
        if (req.getPrice() != null) t.setPrice(req.getPrice());
        if (req.getVisible() != null) t.setVisible(req.getVisible());
        Treatment saved = treatmentRepository.save(t);
        return toDto(saved);
    }

    @Transactional
    public void softDelete(Long id) {
        Treatment t = treatmentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Treatment not found"));
        t.setVisible(false);
        treatmentRepository.save(t);
    }
}

