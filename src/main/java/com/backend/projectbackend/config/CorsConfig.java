package com.backend.projectbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Permitir CORS en todos los endpoints
                        .allowedOrigins("http://localhost:5173")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // Métodos permitidos
                        .allowedHeaders("*") // Permitir todas las cabeceras
                        .allowCredentials(true); // Permitir cookies/autenticaciones (si es necesario)
            }
        };
    }
}
