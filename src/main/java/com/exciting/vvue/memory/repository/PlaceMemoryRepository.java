package com.exciting.vvue.memory.repository;

import com.exciting.vvue.memory.model.PlaceMemory;
import com.exciting.vvue.place.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceMemoryRepository extends JpaRepository<PlaceMemory, Long> {
}
