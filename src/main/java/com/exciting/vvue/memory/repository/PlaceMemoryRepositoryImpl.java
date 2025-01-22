package com.exciting.vvue.memory.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.exciting.vvue.memory.model.PlaceMemory;
import com.exciting.vvue.memory.repository.jpa.PlaceMemoryJpaRepository;
import com.exciting.vvue.memory.service.PlaceMemoryRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PlaceMemoryRepositoryImpl implements PlaceMemoryRepository {
	private final PlaceMemoryJpaRepository placeMemoryJpaRepository;

	@Override
	public PlaceMemory save(PlaceMemory placeMemory) {
		placeMemoryJpaRepository.save(placeMemory);
		return placeMemory;
	}

	@Override
	public List<PlaceMemory> saveAll(List<PlaceMemory> placeMemoryList) {
		return placeMemoryJpaRepository.saveAll(placeMemoryList);
	}

	@Override
	public List<PlaceMemory> findByScheduleMemory_Id(Long scheduleMemoryId) {
		return placeMemoryJpaRepository.findByScheduleMemory_Id(scheduleMemoryId);
	}

	@Override
	public List<PlaceMemory> findAllByMarriedId(Long marriedId){
		return placeMemoryJpaRepository.findAllByMarriedId(marriedId);
	}
}
