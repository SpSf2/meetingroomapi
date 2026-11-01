package com.practicerest.dto.response;
public class ReservationCategoryResponse{

    Long id;
    String name;

    public ReservationCategoryResponse() { }

    public ReservationCategoryResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public void setId(Long id) { this.id = id; }

    public void setName(String name) { this.name = name; }
}