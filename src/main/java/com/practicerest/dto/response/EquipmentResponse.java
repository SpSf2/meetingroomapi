package com.practicerest.dto.response;

public class EquipmentResponse {

    private Long id;
    private String name;

    public EquipmentResponse() {
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
