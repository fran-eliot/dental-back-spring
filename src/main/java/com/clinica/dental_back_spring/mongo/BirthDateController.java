package com.clinica.dental_back_spring.mongo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/birthdates")
@Tag(name = "Fechas de nacimiento (MongoDB)", description = "CRUD en MongoDB")
public class BirthDateController {

    private final BirthDateService birthDateService;

    public BirthDateController(BirthDateService birthDateService) {
        this.birthDateService = birthDateService;
    }

    @Operation(summary = "Guardar o actualizar fecha de nacimiento")
    @PostMapping
    public ResponseEntity<?> save(@RequestBody Map<String, String> body) {
        Long userId = Long.valueOf(body.get("userId"));
        LocalDate birthDate = LocalDate.parse(body.get("birthDate"));
        String note = body.getOrDefault("note", "");

        BirthDateDocument saved = birthDateService.saveBirthDate(userId, birthDate, note);

        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "Obtener fecha de nacimiento por userId")
    @GetMapping("/{userId}")
    public ResponseEntity<?> get(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(birthDateService.getBirthDate(userId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }

    @Operation(summary = "Eliminar fecha de nacimiento de un usuario")
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> delete(@PathVariable Long userId) {
        try {
            birthDateService.deleteBirthDate(userId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }
}
