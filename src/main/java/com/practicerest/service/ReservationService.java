package com.practicerest.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.practicerest.dto.response.EquipmentResponse;
import com.practicerest.dto.response.ReservationCategoryResponse;
import com.practicerest.dto.response.ReservationResponse;
import com.practicerest.entity.Equipment;
import com.practicerest.entity.Reservation;
import com.practicerest.entity.ReservationCategory;
import com.practicerest.exception.ResourceNotFoundException;
import com.practicerest.repository.EquipmentRepository;
import com.practicerest.repository.ReservationCategoryRepository;
import com.practicerest.repository.ReservationRepository;

@Service
public class ReservationService {

    // Instanciamos el Logger profesional para la clase
    private static final Logger log = LoggerFactory.getLogger(ReservationService.class);

    /* Se modifica todo el service con metodos similares a los anteriores, que cumplen
    la misma función, pero que usen el repositorio de la entidad Reservation */
    
    private final ReservationRepository reservationRepository;
    private final ReservationCategoryRepository reservationCategoryRepository;
    private final EquipmentRepository equipmentRepository;

    // Constructor de Repositories
    public ReservationService(ReservationRepository reservationRepository,
                ReservationCategoryRepository reservationCategoryRepository,
                EquipmentRepository equipmentRepository) {
    this.reservationRepository = reservationRepository;
    this.reservationCategoryRepository = reservationCategoryRepository;
    this.equipmentRepository = equipmentRepository;
    }

    /*Comentamos este método y aplicamos el siguiente para implementar Paginación 
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }  */
    @Transactional(readOnly = true)
    public Page<ReservationResponse> getAllReservations(int page, int size, 
                                String sortBy, String direction) {

         Sort sort = direction.equalsIgnoreCase("desc")  //new change: orden by sort 
            ? Sort.by(sortBy).descending()
            : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return reservationRepository.findAll(pageable)
                                    .map(this::mapToResponse);
    }

    //Método para buscar una Reservation por id
    // CAMBIO 1: ya no devuelve Optional, lanza excepción si no encuentra id
    @Transactional(readOnly = true)
    public ReservationResponse getReservationResponseById(Long id) {
        return reservationRepository.findById(id)
                                    .map(this::mapToResponse)
                                    .orElseThrow(() -> new ResourceNotFoundException
                                           ("Reserva no encontrada con id: " + id));
    }
    
    //Método modificado para que acepte y cree una Reserva con categoría y equipos(opcional)
    public ReservationResponse createReservation(Reservation reservation,
                            Long categoryId, List<Long> equipmentIds) {
        
        // --- 0. VALIDACIONES DE FECHAS Y SOLAPAMIENTO ---
        if (reservation.getStartTime() == null || reservation.getEndTime() == null) {
            throw new IllegalArgumentException("La fecha y hora de inicio y fin son obligatorias");
        }

        if (reservation.getEndTime().isBefore(reservation.getStartTime()) || 
            reservation.getEndTime().isEqual(reservation.getStartTime())) {
            throw new IllegalArgumentException("La fecha y hora de fin deben ser posteriores a la de inicio");
        }

        boolean isRoomOccupied = reservationRepository.existsByRoomNameAndStartTimeLessThanAndEndTimeGreaterThan(
            reservation.getRoomName(), 
            reservation.getEndTime(), 
            reservation.getStartTime()
        );

        if (isRoomOccupied) {
            throw new IllegalStateException("La sala '" + reservation.getRoomName() + "' ya se encuentra reservada en ese horario.");
        }
        // ----------------------------------------------

        // 1. Categoría obligatoria y validación de existencia                    
        if (categoryId == null) {
            throw new IllegalArgumentException("El ID de la categoría es obligatorio");
        }
            ReservationCategory category = reservationCategoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException
                                ("Categoría no encontrada con id: " + categoryId));
            reservation.setCategory(category);
        
        // 2. Equipos opcionales con recordatorio/recomendación
        if (equipmentIds == null || equipmentIds.isEmpty()) {

            // Métrica de negocio: Registramos cuando una reserva se crea sin equipos
            log.info("Métrica de negocio: La reserva para la sala '{}' por '{}' se creó sin equipos solicitados.", 
                    reservation.getRoomName(), reservation.getReservedBy());
        } else {
            List<Equipment> equipmentList = equipmentRepository.findAllById(equipmentIds);
           
            // Validamos que todos los equipos solicitados existan
            if (equipmentList.size() != equipmentIds.size()) {
                throw new ResourceNotFoundException
                            ("Uno o más equipos especificados no fueron encontrados");
            }
            reservation.setEquipment(equipmentList);
        }

        Reservation savedReservation = reservationRepository.save(reservation);
        return mapToResponse(savedReservation);
    }

    //Método modificado para que acepte y actualice una Reserva con Dto
     // CAMBIO 2: ya no devuelve Optional, lanza excepción si no encuentra id
    public ReservationResponse updateReservation(Long id, 
        Reservation reservation, Long categoryId, List<Long> equipmentIds) {

        return reservationRepository.findById(id)
                                .map(existingReservation -> {
            existingReservation.setRoomName(reservation.getRoomName());
            existingReservation.setReservedBy(reservation.getReservedBy());
            existingReservation.setStartTime(reservation.getStartTime()); // <-- Añadir
            existingReservation.setEndTime(reservation.getEndTime());         // <-- Añadir

            // 1. Categoría: si se proporciona un ID, se valida y se actualiza; 
            // si es null, se mantiene la categoría que ya tenía la reserva.
            if (categoryId != null) {
                ReservationCategory category = reservationCategoryRepository.findById(categoryId)
                        .orElseThrow(() -> new ResourceNotFoundException
                                ("Categoría no encontrada con id: " + categoryId));
                existingReservation.setCategory(category);
            }
                                    
            // 2. Equipos opcionales con recordatorio/recomendación
            if (equipmentIds == null || equipmentIds.isEmpty()) {

                // Métrica de negocio: Registramos cuando una reserva se Actualiza sin equipos
            log.info("Métrica de negocio: La reserva para la sala '{}' por '{}' " + 
                        " se modificó sin equipos solicitados.", 
                            reservation.getRoomName(), reservation.getReservedBy());
            } else {
                List<Equipment> equipmentList = equipmentRepository.findAllById(equipmentIds);
                if (equipmentList.size() != equipmentIds.size()) {
                    throw new ResourceNotFoundException
                            ("Uno o más equipos especificados no fueron encontrados");
                }
                existingReservation.setEquipment(equipmentList);
            }

            return reservationRepository.save(existingReservation);
        })
        .map(this::mapToResponse)
        .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con id: " + id));
    }

    //Método modificado para que acepte y elimine una categoría 
     // CAMBIO 3: lanza excepción si no encuentra la reserva a eliminar
    public boolean deleteReservation(Long id) {

        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Reserva no encontrada con id: " + id
            );
        }

        reservationRepository.deleteById(id);

        return true;
    }


    private ReservationResponse mapToResponse(Reservation reservation) {
        ReservationResponse response = new ReservationResponse();
        response.setId(reservation.getId());
        response.setRoomName(reservation.getRoomName());
        response.setReservedBy(reservation.getReservedBy());

        // --- MAPEO DE FECHAS ---
        response.setStartTime(reservation.getStartTime());
        response.setEndTime(reservation.getEndTime());
        // -----------------------

        if (reservation.getCategory() != null) {
            ReservationCategoryResponse categoryResponse = new ReservationCategoryResponse();
            categoryResponse.setId(reservation.getCategory().getId());
            categoryResponse.setName(reservation.getCategory().getName());
            response.setCategory(categoryResponse);
        }
        /*
        if (reservation.getEquipment() != null && !reservation.getEquipment().isEmpty()) {
            List<EquipmentResponse> equipmentResponses = new ArrayList<>();

            for (Equipment equipment : reservation.getEquipment()) {
                EquipmentResponse equipmentResponse = new EquipmentResponse();
                equipmentResponse.setId(equipment.getId());
                equipmentResponse.setName(equipment.getName());
                equipmentResponses.add(equipmentResponse);
            }  */
        
          // Equipment usando Streams y el constructor EquipmentResponse(Equipment)
        if (reservation.getEquipment() != null && !reservation.getEquipment().isEmpty()) {
            List<EquipmentResponse> equipmentResponses = reservation.getEquipment().stream()
                    .map(EquipmentResponse::new) // llama al constructor EquipmentResponse(Equipment)
                    .collect(java.util.stream.Collectors.toList());

            response.setEquipment(equipmentResponses);
        }
        return response;
    }


}