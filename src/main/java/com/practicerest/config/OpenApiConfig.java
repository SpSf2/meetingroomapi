package com.practicerest.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Meeting Room API - Gestión de Reservas")
                        .version("1.0.0")
                        .description("API REST desarrollada con"
                                + "Spring Boot para la gestión y reserva de salas " 
                                + " de reuniones, categorías y equipos.")
                        .contact(new Contact()
                                .name("Dug")
                                .email("kiworks.duglas@gmail.com")));
    }
}