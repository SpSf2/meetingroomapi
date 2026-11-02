package com.practicerest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.practicerest.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByCategoryId(Long id);

    @Query("SELECT COUNT(r) > 0 FROM Reservation r JOIN r.equipment e WHERE e.id = :equipmentId")
    boolean existsByEquipmentId(@Param("equipmentId") Long equipmentId);
}

