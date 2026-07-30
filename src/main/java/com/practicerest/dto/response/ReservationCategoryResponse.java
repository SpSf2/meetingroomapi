package com.practicerest.dto.response;

public class ReservationCategoryResponse {

    private Long id;
    private String name;

    public ReservationCategoryResponse() {
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