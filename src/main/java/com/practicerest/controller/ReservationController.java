package com.practicerest.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.practicerest.model.Reservation;

@RestController
public class ReservationController {

  /* Comento este codigo inicial que devuelve un String: "Listado de reservas"
   para comparar con el cambio que se hará, que devolverá un objeto Reservation.
   al realizar la petición Get al localhost:8080/reservations

    @GetMapping("/reservations")
     public String getReservations() {
        return "Listado de reservas";    */
    
   /* 
    @GetMapping("/reservations")
    public Reservation getReservations() {
        return new Reservation(1L, "Sala Norte", "Carlos");
    }
         Este método anterior devuelve un Objeto Reservation:
         {
            "id": 1,
            "roomName": "Sala Norte",
            "reservedBy": "Carlos"
         }
         */

    //Ahora volvemos a modificar para la siguiente prueba:
    /* 
    @GetMapping("/reservations")
    public List<Reservation> getReservations() {
        return List.of(
                new Reservation(1L, "Sala Norte", "Carlos"),
                new Reservation(2L, "Sala Sur", "Lucía"),
                new Reservation(3L, "Sala Este", "Marta")
        );
    }    */
    //El resultado es un Array de Objetos Reservation como era de esperar.


    /*
    //Obtener una reserva específica:
    @GetMapping("/reservations/{id}")
    public Reservation getReservationById(@PathVariable Long id) {
        return new Reservation(id, "Sala Norte", "Carlos");
    }
    este método anterior recibe un id en la petición y lo asigna a lo
     devuelto en el return, solo para entender el flujo */

    // Ahora volvemos a modificar para la prueba final:
    private final List<Reservation> reservations = List.of(
            new Reservation(1L, "Sala Norte", "Carlos"),
            new Reservation(2L, "Sala Sur", "Lucía"),
            new Reservation(3L, "Sala Este", "Marta")
    );

    @GetMapping("/reservations")
    public List<Reservation> getReservations() {
        return reservations;
    }

    /*
    @GetMapping("/reservations/{id}")
    public Reservation getReservationById(@PathVariable Long id) {
        return reservations.stream()
                .filter(reservation -> reservation.getId().equals(id))
                .findFirst()
                .orElse(null);    
    } Este metodo devuelve el objeto Reservation que tiene el id que se le pasa como parametro.
    */

    @GetMapping("/reservations/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        return reservations.stream()
                .filter(reservation -> reservation.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    } 
    //Este método devuelve el status 200 ok ó status 404 no found
}
