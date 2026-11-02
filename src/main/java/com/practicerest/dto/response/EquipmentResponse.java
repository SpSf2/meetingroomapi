package com.practicerest.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.practicerest.entity.Equipment;

@JsonPropertyOrder ({"id", "name", "description"})
public class EquipmentResponse {

    private Long id;
    private String name;
    private String description; 

    public EquipmentResponse() {
    }

     // Constructor que acepta la entidad Equipment
    public EquipmentResponse(Equipment equipment) {
        this.id = equipment.getId();
        this.name = equipment.getName();
        this.description = equipment.getDescription();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
