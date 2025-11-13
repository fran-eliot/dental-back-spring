package com.clinica.dental_back_spring.mongo;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class BirthDateService {

    private final BirthDateRepository birthDateRepository;

    public BirthDateService(BirthDateRepository birthDateRepository) {
        this.birthDateRepository = birthDateRepository;
    }

    public BirthDateDocument saveBirthDate(Long userId, LocalDate birthDate, String note) {
        BirthDateDocument doc = birthDateRepository.findByUserId(userId)
                .orElse(BirthDateDocument.builder()
                        .userId(userId)
                        .build());

        doc.setBirthDate(birthDate);
        doc.setNote(note);

        return birthDateRepository.save(doc);
    }

    public BirthDateDocument getBirthDate(Long userId) {
        return birthDateRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("No hay fecha de nacimiento registrada para ese usuario"));
    }

    public void deleteBirthDate(Long userId) {
        BirthDateDocument doc = birthDateRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("No existe registro"));
        birthDateRepository.delete(doc);
    }
}

