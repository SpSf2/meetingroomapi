package com.practicerest.dto.response;

import com.practicerest.entity.Equipment;

public class EquipmentResponse {

    private Long id;
    private String name;

    public EquipmentResponse() {
    }

     // Constructor que acepta la entidad Equipment
    public EquipmentResponse(Equipment equipment) {
        this.id = equipment.getId();
        this.name = equipment.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
