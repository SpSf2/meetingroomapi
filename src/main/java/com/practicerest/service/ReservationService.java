package com.practicerest.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.practicerest.dto.response.ReservationCategoryResponse;
import com.practicerest.dto.response.ReservationResponse;
import com.practicerest.entity.Reservation;
import com.practicerest.repository.ReservationCategoryRepository;
import com.practicerest.repository.ReservationRepository;

@Service
public class ReservationService {

    /* Se modifica todo el service con metodos similares a los anteriores, que cumplen
    la misma función, pero que usen el repositorio de la entidad Reservation */
    
    private final ReservationRepository reservationRepository;
    private final ReservationCategoryRepository reservationCategoryRepository;

    // Constructor de reservationCategoryRepository
    public ReservationService(ReservationRepository reservationRepository,
                ReservationCategoryRepository reservationCategoryRepository) {
    this.reservationRepository = reservationRepository;
    this.reservationCategoryRepository = reservationCategoryRepository;
    }

    /*Comentamos este método y aplicamos el siguiente para implementar Paginación 
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }  */
    
    public Page<ReservationResponse> getAllReservations(int page, int size, 
                                String sortBy, String direction) {

         Sort sort = direction.equalsIgnoreCase("desc")  //new change: orden by sort 
            ? Sort.by(sortBy).descending()
            : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return reservationRepository.findAll(pageable)
                                    .map(this::mapToResponse);
    }

    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    //Método modificado para que acepte y cree una categoría con Dto
    public ReservationResponse createReservation(Reservation reservation, Long categoryId) {
        if (categoryId != null) {
            reservationCategoryRepository.findById(categoryId)
                                        .ifPresent(reservation::setCategory);
        }

        Reservation savedReservation = reservationRepository.save(reservation);
        return mapToResponse(savedReservation);
    }

    public Optional<ReservationResponse> updateReservation(Long id, Reservation reservation,
                                                       Long categoryId) {

    return reservationRepository.findById(id)
                                .map(existingReservation -> {
            existingReservation.setRoomName(reservation.getRoomName());
            existingReservation.setReservedBy(reservation.getReservedBy());

            if (categoryId != null) {
                reservationCategoryRepository.findById(categoryId)
                                             .ifPresent(existingReservation::setCategory);
            }

            return reservationRepository.save(existingReservation);
        })
        .map(this::mapToResponse);
    }

    public boolean deleteReservation(Long id) {
        if (reservationRepository.existsById(id)) {
            reservationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private ReservationResponse mapToResponse(Reservation reservation) {
        ReservationResponse response = new ReservationResponse();
        response.setId(reservation.getId());
        response.setRoomName(reservation.getRoomName());
        response.setReservedBy(reservation.getReservedBy());

        if (reservation.getCategory() != null) {
            ReservationCategoryResponse categoryResponse = new ReservationCategoryResponse();
            categoryResponse.setId(reservation.getCategory().getId());
            categoryResponse.setName(reservation.getCategory().getName());
            response.setCategory(categoryResponse);
        }
        return response;
    }

    public Optional<ReservationResponse> getReservationResponseById(Long id) {
        return reservationRepository.findById(id)
                                    .map(this::mapToResponse);
    }
}