package com.practicerest.model;

public class Reservation {

    private Long id;
    private String roomName;
    private String reservedBy;

    public Reservation(Long id, String roomName, String reservedBy) {
        this.id = id;
        this.roomName = roomName;
        this.reservedBy = reservedBy;
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
}