package com.practicerest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.practicerest.model.Reservation;
import com.practicerest.repository.ReservationRepository;

@Service
public class ReservationService {

    /* Se modifica todo el service con metodos similares a los anteriores, que cumplen
    la misma función, pero que usen el repositorio de la entidad Reservation */
    
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public Optional<Reservation> updateReservation(Long id, Reservation reservation) {
    
        return reservationRepository.findById(id)
                                    .map(existingReservation -> {
                existingReservation.setRoomName(reservation.getRoomName());
                
        existingReservation.setReservedBy(reservation.getReservedBy());
                return reservationRepository.save(existingReservation);
            });
    }

    public boolean deleteReservation(Long id) {
        if (reservationRepository.existsById(id)) {
            reservationRepository.deleteById(id);
            return true;
        }
        return false;
    }

}