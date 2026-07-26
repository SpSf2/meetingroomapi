package com.practicerest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.practicerest.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}