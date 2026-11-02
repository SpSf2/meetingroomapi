package com.practicerest.service;

import com.practicerest.dto.request.CategoryRequest;
import com.practicerest.dto.response.ReservationCategoryResponse;
import com.practicerest.exception.BadRequestException;
import com.practicerest.exception.ResourceNotFoundException;
import com.practicerest.entity.ReservationCategory;
import com.practicerest.repository.ReservationCategoryRepository;
import com.practicerest.repository.ReservationRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationCategoryService {

    private final ReservationCategoryRepository categoryRepository;
    private final ReservationRepository reservationRepository; // Inyéctalo en el constructor

    public ReservationCategoryService(ReservationCategoryRepository categoryRepository,
                                        ReservationRepository reservationRepository    ) {
        this.categoryRepository = categoryRepository;
        this.reservationRepository = reservationRepository;
    }

    // 1. Listar todas las categorías
    public List<ReservationCategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 2. Buscar categoría por ID
    public ReservationCategoryResponse getCategoryById(Long id) {
        ReservationCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + id));
        return mapToResponse(category);
    }

    // 3. Crear una nueva categoría
    public ReservationCategoryResponse createCategory(CategoryRequest request) {
        ReservationCategory category = new ReservationCategory();
        // Asumiendo que CategoryRequest tiene un método getName() o que es un record con name()
        category.setName(request.getName()); 

        ReservationCategory savedCategory = categoryRepository.save(category);
        return mapToResponse(savedCategory);
    }

    // Actualizar categoría
    public ReservationCategoryResponse updateCategory(Long id, CategoryRequest request) {
        ReservationCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));
        
        category.setName(request.getName());
        ReservationCategory updatedCategory = categoryRepository.save(category);
        
        return mapToResponse(updatedCategory);
    }

    // Eliminar categoría
    public void deleteCategory(Long id) {
        ReservationCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));

        // Verificar directamente en el repositorio si hay reservas usando esta categoría
        boolean hasReservations = reservationRepository.existsByCategoryId(id);

        if (hasReservations) {
            throw new BadRequestException("No se puede eliminar la categoría porque tiene reservas asociadas.");
        }

        categoryRepository.delete(category);
    }

    // Método auxiliar privado para mapear de Entidad a DTO (evita repetir código)
    private ReservationCategoryResponse mapToResponse(ReservationCategory category) {
        ReservationCategoryResponse response = new ReservationCategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        return response;
    }
}
