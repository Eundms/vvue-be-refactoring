package com.exciting.vvue.memory.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.exciting.vvue.memory.model.PlaceMemory;

public interface PlaceMemoryJpaRepository extends JpaRepository<PlaceMemory, Long> {
	@EntityGraph(value="PlaceMemory.withDetails", type = EntityGraph.EntityGraphType.LOAD)
	List<PlaceMemory> findByScheduleMemory_Id(Long scheduleMemoryId);
}
