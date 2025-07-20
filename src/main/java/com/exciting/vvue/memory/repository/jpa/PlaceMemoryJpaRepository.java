package com.exciting.vvue.memory.repository.jpa;

import com.exciting.vvue.memory.model.PlaceMemory;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PlaceMemoryJpaRepository extends JpaRepository<PlaceMemory, Long> {

  @EntityGraph(value = "PlaceMemory.withDetails", type = EntityGraph.EntityGraphType.LOAD)
  List<PlaceMemory> findByScheduleMemory_Id(Long scheduleMemoryId);

  @Query("select pm from PlaceMemory pm where pm.scheduleMemory.married.id = :marriedId")
  List<PlaceMemory> findAllByMarriedId(Long marriedId);
}
