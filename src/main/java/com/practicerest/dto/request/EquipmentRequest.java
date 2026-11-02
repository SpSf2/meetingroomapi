package com.practicerest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para crear o actualizar un equipo")
public class EquipmentRequest {

    @Schema(description = "Nombre del equipo", example = "Proyector HD")
    @NotBlank(message = "El nombre del equipo es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre del equipo debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9 ]+$",
                message = "El campo debe contener solo letras, números y espacios")
    private String name;

    @Schema(description = "Descripción del equipo", example = "Proyector de alta resolución")
    @NotBlank (message = "La descripción del equipo es obligatoria")
    @Size(max = 200, message = "La descripción del equipo no debe exceder los 200 caracteres")
    private String description;

    // Constructor vacío indispensable para Jackson
    public EquipmentRequest() {  }

    public EquipmentRequest(String name, String description) { 
        this.name = name;
        this.description = description;
    }

    // Getters y Setters
    public String getName() { return name; }

    public String getDescription() { return description; }

    public void setName(String name) { this.name = name; }

    public void setDescription(String description) { this.description = description; }
}
