package com.practicerest.controller;

import com.practicerest.dto.request.CategoryRequest;
import com.practicerest.dto.response.ReservationCategoryResponse;
import com.practicerest.service.ReservationCategoryService;
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
@RequestMapping("/categories")
@Tag(name = "Categorías de Reserva", description = "Endpoints para la gestión independiente de categorías de reserva")
public class ReservationCategoryController {

    private final ReservationCategoryService categoryService;

    public ReservationCategoryController(ReservationCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Obtener todas las categorías", description = "Retorna una lista con todas las categorías de reserva registradas")
    @ApiResponse(responseCode = "200", description = "Lista devuelta con éxito")
    @GetMapping
    public ResponseEntity<List<ReservationCategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @Operation(summary = "Obtener categoría por ID", description = "Busca y devuelve una categoría de reserva según su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categoría encontrada"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReservationCategoryResponse> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @Operation(summary = "Crear nueva categoría", description = "Crea una nueva categoría de reserva con las validaciones correspondientes")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Categoría creada con éxito"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<ReservationCategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        ReservationCategoryResponse created = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Actualizar categoría", description = "Actualiza el nombre de una categoría existente según su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categoría actualizada con éxito"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ReservationCategoryResponse> updateCategory(
            @PathVariable Long id, 
            @Valid @RequestBody CategoryRequest request) {
        ReservationCategoryResponse updated = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría de reserva por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Categoría eliminada con éxito"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}