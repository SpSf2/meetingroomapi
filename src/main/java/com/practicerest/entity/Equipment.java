package com.practicerest.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @ManyToMany(mappedBy = "equipment")    
    private List<Reservation> reservations;

    public Equipment() {  }

    public Equipment(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Long getId() { return id; }

    public String getName() { return name;  }

    public String getDescription() { return description;  }

    public void setId(Long id) { this.id = id;  }

    public void setName(String name) { this.name = name;  }

    public void setDescription(String description) { this.description = description;  }

    public List<Reservation> getReservations() {  return reservations;  }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}

