package com.practicerest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.practicerest.entity.Equipment;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

}
