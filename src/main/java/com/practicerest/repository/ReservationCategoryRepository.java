package com.practicerest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.practicerest.entity.ReservationCategory;

public interface ReservationCategoryRepository extends 
                        JpaRepository<ReservationCategory, Long> {
}