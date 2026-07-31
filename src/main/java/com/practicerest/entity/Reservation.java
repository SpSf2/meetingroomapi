package com.practicerest.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomName;
    private String reservedBy;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private ReservationCategory category;

    @ManyToMany  
    @JoinTable(name = "reservation_equipment",
    joinColumns = @JoinColumn(name = "reservation_id"),
    inverseJoinColumns = @JoinColumn(name = "equipment_id"))
    private List<Equipment> equipment;
   
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

    public ReservationCategory getCategory() {
    return category;
    }

    public void setCategory(ReservationCategory category) {
        this.category = category;
    }

    public List<Equipment> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<Equipment> equipment) {
        this.equipment = equipment;
    }
}