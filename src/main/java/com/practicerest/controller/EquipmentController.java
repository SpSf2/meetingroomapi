package com.practicerest.controller;

import com.practicerest.dto.request.EquipmentRequest;
import com.practicerest.dto.response.EquipmentResponse;
import com.practicerest.service.EquipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipments")
@Tag(name = "Equipos", description = "Endpoints para la gestión independiente de equipos de reserva")
public class EquipmentController {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @Operation(summary = "Obtener todos los equipos", description = "Retorna una lista con todos los equipos registrados")
    @ApiResponse(responseCode = "200", description = "Lista devuelta con éxito")
    @GetMapping
    public ResponseEntity<List<EquipmentResponse>> getAllEquipment() {
        return ResponseEntity.ok(equipmentService.getAllEquipment());
    }

    @Operation(summary = "Obtener equipo por ID", description = "Busca y devuelve un equipo específico mediante su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Equipo encontrado"),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EquipmentResponse> getEquipmentById(@PathVariable Long id) {
        return ResponseEntity.ok(equipmentService.getEquipmentById(id));
    }

    @Operation(summary = "Crear nuevo equipo", description = "Registra un nuevo equipo con su respectivo nombre")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Equipo creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<EquipmentResponse> createEquipment(@Valid @RequestBody EquipmentRequest request) {
        EquipmentResponse created = equipmentService.createEquipment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Actualizar equipo", description = "Actualiza el nombre de un equipo existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Equipo actualizado con éxito"),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EquipmentResponse> updateEquipment(
            @PathVariable Long id, @Valid @RequestBody EquipmentRequest request) {
        EquipmentResponse updated = equipmentService.updateEquipment(id, request);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Eliminar equipo", description = "Elimina un equipo si no se encuentra vinculado a ninguna reserva")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Equipo eliminado con éxito"),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado"),
            @ApiResponse(responseCode = "400", description = "No se puede eliminar porque tiene reservas asociadas")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
        equipmentService.deleteEquipment(id);
        return ResponseEntity.noContent().build();
    }
}
