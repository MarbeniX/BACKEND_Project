package com.backend.projectbackend.config; // O tu paquete de configuración preferido

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "ProjectBackend API",
        version = "1.0.0",
        description = "Documentación de la API para el sistema de ProjectBackend. Incluye autenticación y gestión de recursos.",
        contact = @Contact(name = "Tu Nombre/Equipo", email = "tu-email@dominio.com"),
        license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0.html")
    )
)
@SecurityScheme(
    name = "bearerAuth", // Este nombre DEBE COINCIDIR con el usado en @SecurityRequirement
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
public class OpenApiConfig {
}