package com.practicerest.dto.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
@Schema(name = "ReservationRequest", 
        description = "Datos requeridos para crear o actualizar una reserva")
public class ReservationRequest {

    @Schema(description = "Nombre de la sala de reuniones", example = "Sala Norte",
                 minLength = 2, maxLength = 50)
    @NotBlank(message = "Room name is required")
    @Size(min = 2, max = 50, message = "Room name must be between 2 and 50 characters")
    private String roomName;

    @Schema(description = "Persona que realiza la reserva (solo letras y espacios)",
                 example = "Ana Pérez", minLength = 2, maxLength = 50)
    @NotBlank(message = "Reserved by is required")
    @Size(min = 2, max = 50, message = "Reserved by must be between 2 and 50 characters")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$",
    message = "Reserved by must contain only letters and spaces")
    private String reservedBy;

    @Schema(description = "ID de la categoría de la reserva", example = "1")
    @NotNull(message = "Category is required")
    @JsonProperty("categoryId") // <-- Fuerza a Jackson a mapearlo correctamente
    private Long categoryId;

    @Schema(description = "Lista de IDs de equipos solicitados", example = "[10, 11]")
    private List<Long> equipmentIds;

    public ReservationRequest() {  }

    public String getRoomName() {  return roomName; }

    public String getReservedBy() { return reservedBy; }

    public Long getCategoryId() { return categoryId; }

    public void setRoomName(String roomName) { this.roomName = roomName; }

    public void setReservedBy(String reservedBy) { this.reservedBy = reservedBy; }

    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public List<Long> getEquipmentIds() { return equipmentIds; }

    public void setEquipmentIds(List<Long> equipmentIds) { this.equipmentIds = equipmentIds; }
}
