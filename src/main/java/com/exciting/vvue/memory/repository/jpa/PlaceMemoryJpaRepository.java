package com.exciting.vvue.memory.repository.jpa;

import com.exciting.vvue.memory.model.PlaceMemory;
import com.exciting.vvue.place.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PlaceMemoryJpaRepository extends JpaRepository<PlaceMemory, Long> {
}
