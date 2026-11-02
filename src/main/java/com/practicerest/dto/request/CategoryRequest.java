package com.practicerest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para crear o actualizar una categoría de reserva")
public class CategoryRequest {

    @Schema(description = "Nombre de la categoría", example = "Reuniones Ejecutivas")
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$",
                  message = "Category name must contain only letters and spaces")
    private String name;

    // Constructor vacío (necesario para la deserialización de JSON)
    public CategoryRequest() {
    }

    // Constructor con parámetros (opcional)
    public CategoryRequest(String name) {
        this.name = name;
    }

    // Getters y Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
