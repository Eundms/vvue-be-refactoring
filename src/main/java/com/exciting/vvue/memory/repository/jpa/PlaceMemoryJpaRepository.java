package com.exciting.vvue.memory.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exciting.vvue.memory.model.PlaceMemory;

public interface PlaceMemoryJpaRepository extends JpaRepository<PlaceMemory, Long> {
}
