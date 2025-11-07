package com.clinica.dental_back_spring.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI clinicDentalOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("\uD83E\uDDB7 SMYLE: Clínica Dental - API REST BACKEND")
                        .description("""
                                API REST para la gestión integral de una clínica dental.
                                Incluye gestión de pacientes, profesionales, tratamientos,
                                citas y disponibilidades, con autenticación JWT.
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Fran Ramírez")
                                .email("ramirez.martin.francisco@gmail.com")
                                .url("https://github.com/fran-eliot"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Repositorio Backend en GitHub")
                        .url("https://github.com/fran-eliot/dental-back-spring"));
    }
}
