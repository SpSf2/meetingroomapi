package com.practicerest.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "roomName", "reservedBy", "category", "equipment" })
public class ReservationResponse {

    private Long id;
    private String roomName;
    private String reservedBy;

    // --- NUEVOS CAMPOS ---
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    // ---------------------
    private ReservationCategoryResponse category;
    private List<EquipmentResponse> equipment;

    public ReservationResponse() {  }

    public Long getId() { return id; }

    public String getRoomName() { return roomName; }

    public String getReservedBy() { return reservedBy; }

    public LocalDateTime getStartTime() { return startTime; }

    public LocalDateTime getEndTime() { return endTime; }

    public ReservationCategoryResponse getCategory() { return category; }

    public void setId(Long id) { this.id = id; }

    public void setRoomName(String roomName) { this.roomName = roomName; }

    public void setReservedBy(String reservedBy) { this.reservedBy = reservedBy; }

    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public void setCategory(ReservationCategoryResponse category) {
        this.category = category;
    }

    public List<EquipmentResponse> getEquipment() { return equipment; }

    public void setEquipment(List<EquipmentResponse> equipment) {
        this.equipment = equipment;
    }   
}
