package com.practicerest.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.practicerest.dto.request.ReservationRequest;
import com.practicerest.entity.Reservation;
import com.practicerest.service.ReservationService;

import jakarta.validation.Valid;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }
    /*Metodo para obtener todas las reservas lo comentamos para mejorarlo
    @GetMapping("/reservations")
    public List<Reservation> getReservations() {
        return reservationService.getAllReservations();
    }  */

    //Método para obtener las reservas por paginación
    @GetMapping("/reservations")
    public Page<Reservation> getReservations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestParam(defaultValue = "id") String sortBy, //nuevo parametro para ordenar por id
            @RequestParam(defaultValue = "asc") String direction) { // para orden asc

        return reservationService.getAllReservations(page, size, sortBy, direction);
    }

    //Metodo para obtener una reserva por su id
    @GetMapping("/reservations/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //Metodo para crear una reserva
    @PostMapping("/reservations")
    public ResponseEntity<Reservation> createReservation(@Valid @RequestBody ReservationRequest request) {
        Reservation reservation = new Reservation();
        reservation.setRoomName(request.getRoomName());
        reservation.setReservedBy(request.getReservedBy());

        Reservation createdReservation = reservationService.createReservation(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
    }

    //Metodo para actualizar una reserva
    @PutMapping("/reservations/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable Long id,
            @Valid @RequestBody ReservationRequest request) {

        Reservation reservation = new Reservation();
        reservation.setRoomName(request.getRoomName());
        reservation.setReservedBy(request.getReservedBy());

        return reservationService.updateReservation(id, reservation)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //Metodo para eliminar una reserva
   @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        boolean deleted = reservationService.deleteReservation(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}