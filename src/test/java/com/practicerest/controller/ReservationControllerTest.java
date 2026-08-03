package com.practicerest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.ObjectMapper;
import com.practicerest.dto.request.ReservationRequest;
import com.practicerest.dto.response.EquipmentResponse;
import com.practicerest.dto.response.ReservationCategoryResponse;
import com.practicerest.dto.response.ReservationResponse;
import com.practicerest.service.ReservationService;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReservationService reservationService;

    @Test
    void shouldReturnReservationById() throws Exception {
        ReservationCategoryResponse category = new ReservationCategoryResponse();
        category.setId(1L);
        category.setName("Reunión");

        EquipmentResponse equipment = new EquipmentResponse();
        equipment.setId(10L);
        equipment.setName("Proyector");

        ReservationResponse response = new ReservationResponse();
        response.setId(100L);
        response.setRoomName("Sala Norte");
        response.setReservedBy("Ana");
        response.setCategory(category);
        response.setEquipment(List.of(equipment));

        when(reservationService.getReservationResponseById(100L))
                .thenReturn(Optional.of(response));

        mockMvc.perform(get("/reservations/100"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.roomName").value("Sala Norte"))
                .andExpect(jsonPath("$.reservedBy").value("Ana"))
                .andExpect(jsonPath("$.category.id").value(1))
                .andExpect(jsonPath("$.category.name").value("Reunión"))
                .andExpect(jsonPath("$.equipment[0].id").value(10))
                .andExpect(jsonPath("$.equipment[0].name").value("Proyector"));
    }

    @Test
    void shouldReturnNotFoundWhenReservationDoesNotExist() throws Exception {
        when(reservationService.getReservationResponseById(999L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/reservations/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateReservation() throws Exception {
        ReservationRequest request = new ReservationRequest();
        request.setRoomName("Sala Sur");
        request.setReservedBy("Luis");
        request.setCategoryId(1L);
        request.setEquipmentIds(List.of(10L, 11L));

        ReservationCategoryResponse category = new ReservationCategoryResponse();
        category.setId(1L);
        category.setName("Reunión");

        EquipmentResponse equipment1 = new EquipmentResponse();
        equipment1.setId(10L);
        equipment1.setName("Proyector");

        EquipmentResponse equipment2 = new EquipmentResponse();
        equipment2.setId(11L);
        equipment2.setName("Portátil");

        ReservationResponse response = new ReservationResponse();
        response.setId(101L);
        response.setRoomName("Sala Sur");
        response.setReservedBy("Luis");
        response.setCategory(category);
        response.setEquipment(List.of(equipment1, equipment2));

        when(reservationService.createReservation(any(), eq(1L), eq(List.of(10L, 11L))))
                .thenReturn(response);

        mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(101))
                .andExpect(jsonPath("$.roomName").value("Sala Sur"))
                .andExpect(jsonPath("$.reservedBy").value("Luis"))
                .andExpect(jsonPath("$.category.id").value(1))
                .andExpect(jsonPath("$.equipment[0].name").value("Proyector"));
    }

    @Test
    void shouldUpdateReservation() throws Exception {
        ReservationRequest request = new ReservationRequest();
        request.setRoomName("Sala Editada");
        request.setReservedBy("Marta");
        request.setCategoryId(2L);
        request.setEquipmentIds(List.of(20L));

        ReservationCategoryResponse category = new ReservationCategoryResponse();
        category.setId(2L);
        category.setName("Conferencia");

        EquipmentResponse equipment = new EquipmentResponse();
        equipment.setId(20L);
        equipment.setName("Pantalla");

        ReservationResponse response = new ReservationResponse();
        response.setId(200L);
        response.setRoomName("Sala Editada");
        response.setReservedBy("Marta");
        response.setCategory(category);
        response.setEquipment(List.of(equipment));

        when(reservationService.updateReservation(eq(200L), any(), eq(2L), eq(List.of(20L))))
                .thenReturn(Optional.of(response));

        mockMvc.perform(put("/reservations/200")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(200))
                .andExpect(jsonPath("$.roomName").value("Sala Editada"))
                .andExpect(jsonPath("$.reservedBy").value("Marta"))
                .andExpect(jsonPath("$.category.name").value("Conferencia"))
                .andExpect(jsonPath("$.equipment[0].name").value("Pantalla"));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistingReservation() throws Exception {
        ReservationRequest request = new ReservationRequest();
        request.setRoomName("Sala Editada");
        request.setReservedBy("Marta");
        request.setCategoryId(2L);
        request.setEquipmentIds(List.of(20L));

        when(reservationService.updateReservation(eq(999L), any(), eq(2L), eq(List.of(20L))))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/reservations/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteReservation() throws Exception {
        when(reservationService.deleteReservation(300L)).thenReturn(true);

        mockMvc.perform(delete("/reservations/300"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingReservation() throws Exception {
        when(reservationService.deleteReservation(999L)).thenReturn(false);

        mockMvc.perform(delete("/reservations/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnPagedReservations() throws Exception {
        ReservationResponse response = new ReservationResponse();
        response.setId(1L);
        response.setRoomName("Sala Norte");
        response.setReservedBy("Ana");

        when(reservationService.getAllReservations(0, 2, "id", "asc"))
                .thenReturn(new PageImpl<>(List.of(response)));

        mockMvc.perform(get("/reservations")
                .param("page", "0")
                .param("size", "2")
                .param("sortBy", "id")
                .param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].roomName").value("Sala Norte"))
                .andExpect(jsonPath("$.content[0].reservedBy").value("Ana"));
    }

    @Test
    void shouldReturnBadRequestWhenCreatingReservationWithInvalidData() throws Exception {
        ReservationRequest request = new ReservationRequest();
        request.setRoomName("A");
        request.setReservedBy("123");
        request.setCategoryId(1L);
        request.setEquipmentIds(List.of(10L, 11L));

        mockMvc.perform(post("/reservations")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingReservationWithInvalidData() throws Exception {
        ReservationRequest request = new ReservationRequest();
        request.setRoomName("A");
        request.setReservedBy("123");
        request.setCategoryId(2L);
        request.setEquipmentIds(List.of(20L));

        mockMvc.perform(put("/reservations/200")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnValidationMessagesWhenCreatingReservationWithInvalidData() throws Exception {
        ReservationRequest request = new ReservationRequest();
        request.setRoomName("A");
        request.setReservedBy("123");
        request.setCategoryId(1L);
        request.setEquipmentIds(List.of(10L, 11L));

        mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.roomName").value("Room name must be between 2 and 50 characters"))
                .andExpect(jsonPath("$.reservedBy").value("Reserved by must contain only letters and spaces"));
    }

    @Test
    void shouldReturnValidationMessagesWhenUpdatingReservationWithInvalidData() throws Exception {
        ReservationRequest request = new ReservationRequest();
        request.setRoomName("A");
        request.setReservedBy("123");
        request.setCategoryId(2L);
        request.setEquipmentIds(List.of(20L));

        mockMvc.perform(put("/reservations/200")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.roomName").value("Room name must be between 2 and 50 characters"))
        .andExpect(jsonPath("$.reservedBy").value("Reserved by must contain only letters and spaces"));
    }

}
