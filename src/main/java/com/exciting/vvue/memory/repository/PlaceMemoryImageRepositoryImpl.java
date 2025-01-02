package com.exciting.vvue.memory.repository;

import com.exciting.vvue.memory.service.PlaceMemoryImageRepository;
import com.exciting.vvue.memory.model.PlaceMemoryImage;
import com.exciting.vvue.memory.repository.jpa.PlaceMemoryImageJpaRepository;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PlaceMemoryImageRepositoryImpl implements PlaceMemoryImageRepository {
	private final PlaceMemoryImageJpaRepository placeMemoryImageJpaRepository;

	@Override
	public void save(PlaceMemoryImage placeMemoryImage) {
		placeMemoryImageJpaRepository.save(placeMemoryImage);
	}
}

