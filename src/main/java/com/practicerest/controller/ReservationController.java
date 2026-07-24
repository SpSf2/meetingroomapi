package com.practicerest.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

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
     devuelto en el return, esto es solo para entender el flujo */

    /* Ahora volvemos a modificar para la prueba siguiente:
    private final List<Reservation> reservations = List.of(
            new Reservation(1L, "Sala Norte", "Carlos"),
            new Reservation(2L, "Sala Sur", "Lucía"),
            new Reservation(3L, "Sala Este", "Marta")
    ); 
        Esta lista la vamos a modificar despues de crear el metodo "Post"
    por una ArrayList que permite agregar nuevas reservas
    ya que List.of es inmutable y no permite agregar elementos a la lista. */

    private final List<Reservation> reservations = new ArrayList<>(List.of(
        new Reservation(1L, "Sala Norte", "Carlos"),
        new Reservation(2L, "Sala Sur", "Lucía"),
        new Reservation(3L, "Sala Este", "Marta")
    ));

    // Este método devuelve una lista de reservas.
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
    } Este metodo devuelve el objeto Reservation por el id que se le pasa 
     como parametro en el navegador.
    */

    @GetMapping("/reservations/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        return reservations.stream()
                .filter(reservation -> reservation.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    } 
    //Este método devuelve lo mismo que el anterior con el status 200 ok ó 
    // status 404 no found, en caso de que no exista la reserva con ese id.

    /* Metodo para crear una reserva, recibiendo en el Body y devolviendo la salida en Json: 
    @PostMapping("/reservations")
    public Reservation createReservation(@RequestBody Reservation reservation) {
        return reservation;
    }       */

    /*Modificación del metodo anterior para devolver el status 201 Created */
   @PostMapping("/reservations")
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {

        reservations.add(reservation); //Nueva modificacion: Agregar la reserva recibida al ArrayList
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }
}
