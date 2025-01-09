package com.exciting.vvue.memory.repository;

import java.util.List;
import org.springframework.stereotype.Repository;

import com.exciting.vvue.memory.model.PlaceMemoryImage;
import com.exciting.vvue.memory.repository.jpa.PlaceMemoryImageJpaRepository;
import com.exciting.vvue.memory.service.PlaceMemoryImageRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PlaceMemoryImageRepositoryImpl implements PlaceMemoryImageRepository {
	private final PlaceMemoryImageJpaRepository placeMemoryImageJpaRepository;

	@Override
	public void save(PlaceMemoryImage placeMemoryImage) {
		placeMemoryImageJpaRepository.save(placeMemoryImage);
	}

	@Override
	public void saveAll(List<PlaceMemoryImage> placeMemoryImages) {
		placeMemoryImageJpaRepository.saveAll(placeMemoryImages);
	}
}

