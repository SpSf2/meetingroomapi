package com.practicerest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ReservationRequest {

    @NotBlank(message = "Room name is required")
    @Size(min = 2, max = 50, message = "Room name must be between 2 and 50 characters")
    private String roomName;

    @NotBlank(message = "Reserved by is required")
    @Size(min = 2, max = 50, message = "Reserved by must be between 2 and 50 characters")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$",
    message = "Reserved by must contain only letters and spaces")
    private String reservedBy;

    private Long categoryId;

    public ReservationRequest() {
    }

    public String getRoomName() {
        return roomName;
    }

    public String getReservedBy() {
        return reservedBy;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setReservedBy(String reservedBy) {
        this.reservedBy = reservedBy;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
