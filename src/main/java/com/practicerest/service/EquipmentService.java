package com.practicerest.service;

import com.practicerest.dto.request.EquipmentRequest;
import com.practicerest.dto.response.EquipmentResponse;
import com.practicerest.exception.BadRequestException;
import com.practicerest.exception.ResourceNotFoundException;
import com.practicerest.entity.Equipment;
import com.practicerest.repository.EquipmentRepository;
import com.practicerest.repository.ReservationRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final ReservationRepository reservationRepository;

    public EquipmentService(EquipmentRepository equipmentRepository, ReservationRepository reservationRepository) {
        this.equipmentRepository = equipmentRepository;
        this.reservationRepository = reservationRepository;
    }

    // 1. Listar todos los equipos
    public List<EquipmentResponse> getAllEquipment() {
        return equipmentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 2. Buscar equipo por ID
    public EquipmentResponse getEquipmentById(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipo no encontrado con ID: " + id));
        return mapToResponse(equipment);
    }

    // 3. Crear un nuevo equipo
    public EquipmentResponse createEquipment(EquipmentRequest request) {
        Equipment equipment = new Equipment();
        equipment.setName(request.getName());
        equipment.setDescription(request.getDescription());
        Equipment savedEquipment = equipmentRepository.save(equipment);
        return mapToResponse(savedEquipment);
    }

    // 4. Actualizar un equipo existente
    public EquipmentResponse updateEquipment(Long id, EquipmentRequest request) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipo no encontrado con ID: " + id));
        
        equipment.setName(request.getName());
        equipment.setDescription(request.getDescription());
        Equipment updatedEquipment = equipmentRepository.save(equipment);
        return mapToResponse(updatedEquipment);
    }

    // 5. Eliminar un equipo con validación de negocio segura
    public void deleteEquipment(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipo no encontrado con ID: " + id));
        
        // 2. Verificar existencia mediante la consulta directa (evita el LazyInitializationException)
        boolean hasReservations = reservationRepository.existsByEquipmentId(id);
        
        if (hasReservations) {
            throw new BadRequestException("No se puede eliminar el equipo porque está asociado a una o más reservas.");
        }
        
        equipmentRepository.delete(equipment);
    }

    // Método auxiliar privado para mapear de Entidad a DTO
    private EquipmentResponse mapToResponse(Equipment equipment) {
        EquipmentResponse response = new EquipmentResponse();
        response.setId(equipment.getId());
        response.setName(equipment.getName());
        response.setDescription(equipment.getDescription());
        return response;
    }
}
