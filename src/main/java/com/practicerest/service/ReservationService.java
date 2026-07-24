package com.practicerest.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.practicerest.model.Reservation;

@Service
public class ReservationService {

    private final List<Reservation> reservations = new ArrayList<>(List.of(
            new Reservation(1L, "Sala Norte", "Carlos"),
            new Reservation(2L, "Sala Sur", "Lucía"),
            new Reservation(3L, "Sala Este", "Marta")
    ));

    public List<Reservation> getAllReservations() {
        return reservations;
    }

    public Optional<Reservation> getReservationById(Long id) {
        return reservations.stream()
                .filter(reservation -> reservation.getId().equals(id))
                .findFirst();
    }

    public Reservation createReservation(Reservation reservation) {
        reservations.add(reservation);
        return reservation;
    }
}