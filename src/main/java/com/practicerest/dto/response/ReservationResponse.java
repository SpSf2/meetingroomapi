package com.practicerest.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "roomName", "reservedBy", "category" })
public class ReservationResponse {

    private Long id;
    private String roomName;
    private String reservedBy;
    private ReservationCategoryResponse category;

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
}
