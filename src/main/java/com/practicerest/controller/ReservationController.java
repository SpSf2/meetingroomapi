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
import com.practicerest.dto.response.ReservationResponse;
import com.practicerest.entity.Reservation;
import com.practicerest.service.ReservationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Reservations", description = "Endpoints para la gestión de reservas de salas de reuniones")
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
    @Operation(summary = "Obtener todas las reservas paginadas", 
                description = "Permite listar las reservas aplicando paginación y ordenamiento por campos.")
    @GetMapping("/reservations")
    public Page<ReservationResponse> getReservations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestParam(defaultValue = "id") String sortBy, //nuevo parametro para ordenar por id
            @RequestParam(defaultValue = "asc") String direction) { // para orden asc

        return reservationService.getAllReservations(page, size, sortBy, direction);
    }

    //Metodo para obtener una reserva por su id
    @Operation(summary = "Obtener una reserva por su ID", 
                description = "Busca una reserva específica mediante su identificador único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva encontrada exitosamente", 
                     content = @Content(schema = @Schema(implementation = ReservationResponse.class))),
        @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @GetMapping("/reservations/{id}")
    public ResponseEntity<ReservationResponse> getReservationById
                                        (@PathVariable Long id) {
        ReservationResponse response =  reservationService.getReservationResponseById(id);
        return ResponseEntity.ok(response);
    }

    //Metodo para crear una reserva(se agregó Response)
    @Operation(summary = "Crear una nueva reserva",
                 description = "Registra una nueva reserva validando la sala, categoría y equipos asociados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Reserva creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados")
    })
    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createReservation
                                    (@Valid @RequestBody ReservationRequest request) {
        
        Reservation reservation = new Reservation();
        reservation.setRoomName(request.getRoomName());
        reservation.setReservedBy(request.getReservedBy());
        reservation.setStartTime(request.getStartTime()); // <-- ¡Asegúrate de incluir esto!
        reservation.setEndTime(request.getEndTime());     // <-- ¡Y esto!
        
        ReservationResponse createdReservation = reservationService.createReservation(reservation,
                                            request.getCategoryId(), request.getEquipmentIds());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
    }

    //Metodo para actualizar una reserva
    @Operation(summary = "Actualizar una reserva existente", 
                description = "Modifica los datos de una reserva usando su ID.")
    @PutMapping("/reservations/{id}")
    public ResponseEntity<ReservationResponse> updateReservation
        (@PathVariable Long id, @Valid @RequestBody ReservationRequest request) {

        Reservation reservation = new Reservation();
        reservation.setRoomName(request.getRoomName());
        reservation.setReservedBy(request.getReservedBy());
        reservation.setStartTime(request.getStartTime()); // <-- ¡Asegúrate de incluir esto!
        reservation.setEndTime(request.getEndTime());     // <-- ¡Y esto!

            ReservationResponse response =
                reservationService.updateReservation(
                        id,
                        reservation,
                        request.getCategoryId(),
                        request.getEquipmentIds()
            );

        return ResponseEntity.ok(response);
    }

    //Metodo para eliminar una reserva
    @Operation(summary = "Eliminar una reserva", 
                description = "Borra una reserva del sistema mediante su ID.")
    @DeleteMapping("/reservations/{id}")    
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

}