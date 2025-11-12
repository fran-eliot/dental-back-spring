package com.clinica.dental_back_spring.config;

import com.clinica.dental_back_spring.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 🔐 Usa BCrypt para encriptar contraseñas
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                      //  .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // 🔓 Endpoints públicos (sin token)
                        .requestMatchers(
                                "/auth/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/api-docs/**",
                                "/v3/api-docs/**",
                                "/error"
                        ).permitAll()

                        // 👥 Gestión de usuarios (solo ADMIN)
                        .requestMatchers("/users/**").hasRole("ADMIN")

                        // 👨‍⚕️ Profesionales (solo ADMIN)
                        .requestMatchers("/professionals/**").hasRole("ADMIN")

                        // 💊 Tratamientos (solo ADMIN)
                        .requestMatchers("/treatments/**").hasRole("ADMIN")

                        // 👥 Pacientes (ADMIN y DENTISTA)
                        .requestMatchers("/patients/**").hasAnyRole("ADMIN", "DENTISTA")

                        // 📅 Citas (todos los roles principales)
                        .requestMatchers("/appointments/**").hasAnyRole("ADMIN", "DENTISTA", "PACIENTE")

                        // 🕒 Disponibilidades (ADMIN y DENTISTA)
                        .requestMatchers("/availabilities/**").hasAnyRole("ADMIN", "DENTISTA")

                        // ⏰ Slots (solo ADMIN)
                        .requestMatchers("/slots/**").hasRole("ADMIN")

                        // 🔒 Cualquier otro endpoint
                        .anyRequest().authenticated()
                )
                // Filtro JWT antes de UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}