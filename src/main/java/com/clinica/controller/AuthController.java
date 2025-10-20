package com.clinica.controller;

import com.clinica.entity.User;
import com.clinica.repository.UserRepository;
import com.clinica.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register-patient")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        if (userRepo.findByEmail(email).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email already exists"));
        }
        User u = User.builder()
                .email(email)
                .passwordHash(passwordEncoder.encode(password))
                .role(com.clinica.entity.Role.PACIENTE)
                .active(true)
                .build();
        userRepo.save(u);
        return ResponseEntity.ok(Map.of("message", "created"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> body) {
        String email = body.get("email");
        String password = body.get("password");
        return userRepo.findByEmail(email).map(user -> {
            if (!passwordEncoder.matches(password, user.getPasswordHash())) {
                return ResponseEntity.status(401).body(Map.of("message", "invalid credentials"));
            }
            String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getId());
            return ResponseEntity.ok(Map.of("token", token));
        }).orElse(ResponseEntity.status(401).body(Map.of("message", "invalid credentials")));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }
        String token = authHeader.substring(7);
        var claims = jwtUtil.extractAllClaims(token);
        return ResponseEntity.ok(Map.of(
                "email", claims.getSubject(),
                "role", claims.get("role"),
                "userId", claims.get("userId")
        ));
    }
}

