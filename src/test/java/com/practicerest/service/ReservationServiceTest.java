package com.practicerest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.practicerest.dto.response.ReservationResponse;
import com.practicerest.entity.Equipment;
import com.practicerest.entity.Reservation;
import com.practicerest.entity.ReservationCategory;
import com.practicerest.repository.EquipmentRepository;
import com.practicerest.repository.ReservationCategoryRepository;
import com.practicerest.repository.ReservationRepository;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationCategoryRepository reservationCategoryRepository;

    @Mock
    private EquipmentRepository equipmentRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void shouldCreateReservationWithCategoryAndEquipment() {
        Reservation reservation = new Reservation();
        reservation.setRoomName("Sala Norte");
        reservation.setReservedBy("Ana");

        ReservationCategory category = new ReservationCategory();
        category.setId(1L);
        category.setName("Reunión");

        Equipment equipment1 = new Equipment();
        equipment1.setId(10L);
        equipment1.setName("Proyector");

        Equipment equipment2 = new Equipment();
        equipment2.setId(11L);
        equipment2.setName("Portátil");

        Reservation savedReservation = new Reservation();
        savedReservation.setId(100L);
        savedReservation.setRoomName("Sala Norte");
        savedReservation.setReservedBy("Ana");
        savedReservation.setCategory(category);
        savedReservation.setEquipment(List.of(equipment1, equipment2));

        when(reservationCategoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(equipmentRepository.findAllById(List.of(10L, 11L)))
                .thenReturn(List.of(equipment1, equipment2));
        when(reservationRepository.save(reservation)).thenReturn(savedReservation);

        ReservationResponse result = reservationService.createReservation(
                reservation, 1L, List.of(10L, 11L));

        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals("Sala Norte", result.getRoomName());
        assertEquals("Ana", result.getReservedBy());
        assertNotNull(result.getCategory());
        assertEquals(1L, result.getCategory().getId());
        assertEquals("Reunión", result.getCategory().getName());
        assertNotNull(result.getEquipment());
        assertEquals(2, result.getEquipment().size());
        assertEquals("Proyector", result.getEquipment().get(0).getName());
        assertEquals("Portátil", result.getEquipment().get(1).getName());

        verify(reservationCategoryRepository).findById(1L);
        verify(equipmentRepository).findAllById(List.of(10L, 11L));
        verify(reservationRepository).save(reservation);
    }

    @Test
    void shouldCreateReservationWithoutCategoryAndEquipment() {
        Reservation reservation = new Reservation();
        reservation.setRoomName("Sala Sur");
        reservation.setReservedBy("Luis");

        Reservation savedReservation = new Reservation();
        savedReservation.setId(101L);
        savedReservation.setRoomName("Sala Sur");
        savedReservation.setReservedBy("Luis");

        when(reservationRepository.save(reservation)).thenReturn(savedReservation);

        ReservationResponse result = reservationService.createReservation(reservation, null, null);

        assertNotNull(result);
        assertEquals(101L, result.getId());
        assertEquals("Sala Sur", result.getRoomName());
        assertEquals("Luis", result.getReservedBy());
        assertEquals(null, result.getCategory());
        assertEquals(null, result.getEquipment());

        verify(reservationRepository).save(reservation);
    }

    @Test
    void shouldReturnReservationResponseById() {
        ReservationCategory category = new ReservationCategory();
        category.setId(1L);
        category.setName("Reunión");

        Equipment equipment1 = new Equipment();
        equipment1.setId(10L);
        equipment1.setName("Proyector");

        Equipment equipment2 = new Equipment();
        equipment2.setId(11L);
        equipment2.setName("Portátil");

        Reservation reservation = new Reservation();
        reservation.setId(200L);
        reservation.setRoomName("Sala Centro");
        reservation.setReservedBy("Marta");
        reservation.setCategory(category);
        reservation.setEquipment(List.of(equipment1, equipment2));

        when(reservationRepository.findById(200L)).thenReturn(Optional.of(reservation));

        Optional<ReservationResponse> result = reservationService.getReservationResponseById(200L);

        assertNotNull(result);
        assertEquals(true, result.isPresent());
        assertEquals(200L, result.get().getId());
        assertEquals("Sala Centro", result.get().getRoomName());
        assertEquals("Marta", result.get().getReservedBy());
        assertNotNull(result.get().getCategory());
        assertEquals(1L, result.get().getCategory().getId());
        assertEquals("Reunión", result.get().getCategory().getName());
        assertNotNull(result.get().getEquipment());
        assertEquals(2, result.get().getEquipment().size());
        assertEquals("Proyector", result.get().getEquipment().get(0).getName());
        assertEquals("Portátil", result.get().getEquipment().get(1).getName());

        verify(reservationRepository).findById(200L);
    }

    @Test
    void shouldReturnEmptyWhenReservationDoesNotExist() {
        when(reservationRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<ReservationResponse> result = reservationService.getReservationResponseById(999L);

        assertNotNull(result);
        assertEquals(false, result.isPresent());

        verify(reservationRepository).findById(999L);
    }

    @Test
    void shouldUpdateReservationWithCategoryAndEquipment() {
        Reservation existingReservation = new Reservation();
        existingReservation.setId(300L);
        existingReservation.setRoomName("Sala Antigua");
        existingReservation.setReservedBy("Carlos");

        Reservation updatedData = new Reservation();
        updatedData.setRoomName("Sala Nueva");
        updatedData.setReservedBy("Lucía");

        ReservationCategory category = new ReservationCategory();
        category.setId(2L);
        category.setName("Conferencia");

        Equipment equipment1 = new Equipment();
        equipment1.setId(20L);
        equipment1.setName("Pantalla");

        Equipment equipment2 = new Equipment();
        equipment2.setId(21L);
        equipment2.setName("Micrófono");

        Reservation savedReservation = new Reservation();
        savedReservation.setId(300L);
        savedReservation.setRoomName("Sala Nueva");
        savedReservation.setReservedBy("Lucía");
        savedReservation.setCategory(category);
        savedReservation.setEquipment(List.of(equipment1, equipment2));

        when(reservationRepository.findById(300L)).thenReturn(Optional.of(existingReservation));
        when(reservationCategoryRepository.findById(2L)).thenReturn(Optional.of(category));
        when(equipmentRepository.findAllById(List.of(20L, 21L))).thenReturn(List.of(equipment1, equipment2));
        when(reservationRepository.save(existingReservation)).thenReturn(savedReservation);

        Optional<ReservationResponse> result = reservationService.updateReservation(
                300L, updatedData, 2L, List.of(20L, 21L));

        assertNotNull(result);
        assertEquals(true, result.isPresent());
        assertEquals(300L, result.get().getId());
        assertEquals("Sala Nueva", result.get().getRoomName());
        assertEquals("Lucía", result.get().getReservedBy());
        assertNotNull(result.get().getCategory());
        assertEquals(2L, result.get().getCategory().getId());
        assertEquals("Conferencia", result.get().getCategory().getName());
        assertNotNull(result.get().getEquipment());
        assertEquals(2, result.get().getEquipment().size());
        assertEquals("Pantalla", result.get().getEquipment().get(0).getName());
        assertEquals("Micrófono", result.get().getEquipment().get(1).getName());

        verify(reservationRepository).findById(300L);
        verify(reservationCategoryRepository).findById(2L);
        verify(equipmentRepository).findAllById(List.of(20L, 21L));
        verify(reservationRepository).save(existingReservation);
    }

    @Test
    void shouldReturnEmptyWhenUpdatingNonExistingReservation() {
        Reservation updatedData = new Reservation();
        updatedData.setRoomName("Sala Nueva");
        updatedData.setReservedBy("Lucía");

        when(reservationRepository.findById(300L)).thenReturn(Optional.empty());

        Optional<ReservationResponse> result = reservationService.updateReservation(
                300L, updatedData, 2L, List.of(20L, 21L));

        assertNotNull(result);
        assertEquals(false, result.isPresent());

        verify(reservationRepository).findById(300L);
    }

    @Test
    void shouldKeepCategoryAndEquipmentWhenUpdateReceivesNullOrEmpty() {
        Reservation existingReservation = new Reservation();
        existingReservation.setId(400L);
        existingReservation.setRoomName("Sala Original");
        existingReservation.setReservedBy("Pedro");

        ReservationCategory category = new ReservationCategory();
        category.setId(3L);
        category.setName("Formación");

        Equipment equipment1 = new Equipment();
        equipment1.setId(30L);
        equipment1.setName("Pizarra");

        Equipment equipment2 = new Equipment();
        equipment2.setId(31L);
        equipment2.setName("Altavoz");

        existingReservation.setCategory(category);
        existingReservation.setEquipment(List.of(equipment1, equipment2));

        Reservation updatedData = new Reservation();
        updatedData.setRoomName("Sala Editada");
        updatedData.setReservedBy("Pedro Actualizado");

        Reservation savedReservation = new Reservation();
        savedReservation.setId(400L);
        savedReservation.setRoomName("Sala Editada");
        savedReservation.setReservedBy("Pedro Actualizado");
        savedReservation.setCategory(category);
        savedReservation.setEquipment(List.of(equipment1, equipment2));

        when(reservationRepository.findById(400L)).thenReturn(Optional.of(existingReservation));
        when(reservationRepository.save(existingReservation)).thenReturn(savedReservation);

        Optional<ReservationResponse> result = reservationService.updateReservation(
                400L, updatedData, null, List.of());

        assertNotNull(result);
        assertEquals(true, result.isPresent());
        assertEquals(400L, result.get().getId());
        assertEquals("Sala Editada", result.get().getRoomName());
        assertEquals("Pedro Actualizado", result.get().getReservedBy());
        assertNotNull(result.get().getCategory());
        assertEquals(3L, result.get().getCategory().getId());
        assertEquals("Formación", result.get().getCategory().getName());
        assertNotNull(result.get().getEquipment());
        assertEquals(2, result.get().getEquipment().size());
        assertEquals("Pizarra", result.get().getEquipment().get(0).getName());
        assertEquals("Altavoz", result.get().getEquipment().get(1).getName());

        verify(reservationRepository).findById(400L);
        verify(reservationRepository).save(existingReservation);
    }

    @Test
    void shouldDeleteReservationWhenItExists() {
        when(reservationRepository.existsById(500L)).thenReturn(true);

        boolean result = reservationService.deleteReservation(500L);

        assertEquals(true, result);

        verify(reservationRepository).existsById(500L);
        verify(reservationRepository).deleteById(500L);
    }

    @Test
    void shouldReturnFalseWhenDeletingNonExistingReservation() {
        when(reservationRepository.existsById(500L)).thenReturn(false);

        boolean result = reservationService.deleteReservation(500L);

        assertEquals(false, result);

        verify(reservationRepository).existsById(500L);
    }
}
