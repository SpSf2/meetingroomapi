package com.practicerest.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "roomName", "reservedBy", "category", "equipment" })
public class ReservationResponse {

    private Long id;
    private String roomName;
    private String reservedBy;
    private ReservationCategoryResponse category;
    private List<EquipmentResponse> equipment;

    public ReservationResponse() {
    }

    public Long getId() {
        return id;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getReservedBy() {
        return reservedBy;
    }

    public ReservationCategoryResponse getCategory() {
        return category;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setReservedBy(String reservedBy) {
        this.reservedBy = reservedBy;
    }

    public void setCategory(ReservationCategoryResponse category) {
        this.category = category;
    }

    public List<EquipmentResponse> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<EquipmentResponse> equipment) {
        this.equipment = equipment;
    }   
}
