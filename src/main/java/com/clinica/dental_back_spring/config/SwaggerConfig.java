package com.clinica.dental_back_spring.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.info.Contact;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "\uD83E\uDDB7 SMYLE: Clínica Dental - API REST BACKEND",
                version = "1.0.0",
                description = "API REST para la gestión integral de una clínica dental.\n" +
                        "                                Incluye gestión de pacientes, profesionales, tratamientos,\n" +
                        "                                citas y disponibilidades, con autenticación JWT.",
                contact = @Contact(
                        name = "Fran Ramírez Martín",
                        email = "ramirez.martin.francisco@gmail.com",
                        url = "https://github.com/fran-eliot"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Repositorio completo del proyecto",
                url = "https://github.com/fran-eliot/dental-back-spring"
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Servidor de desarrollo local"),
                @Server(url = "https://api.smyleclinic.com", description = "Servidor de producción (ejemplo futuro)")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT"
)
public class SwaggerConfig {
}
