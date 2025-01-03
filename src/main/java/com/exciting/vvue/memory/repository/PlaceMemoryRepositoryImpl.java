package com.exciting.vvue.memory.repository;

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
}
