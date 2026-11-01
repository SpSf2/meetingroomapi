package com.practicerest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para crear o actualizar una categoría de reserva")
public record CategoryRequest(
    @Schema(description = "Nombre de la categoría", example = "Reuniones Ejecutivas")
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(min = 2, max = 50, message = "Category name must be between 2 and 50 characters")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$",
                 message = "Category name must contain only letters and spaces")
    String name
) {}
