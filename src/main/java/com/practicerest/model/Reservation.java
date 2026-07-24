package com.practicerest.model;

public class Reservation {

    private Long id;
    private String roomName;
    private String reservedBy;

    public Reservation() {
    }

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

    public void setId(Long id) {
        this.id = id;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setReservedBy(String reservedBy) {
        this.reservedBy = reservedBy;
    }
}