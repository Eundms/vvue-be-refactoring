package com.exciting.vvue.place.service;

import java.util.List;
import java.util.Optional;

import com.exciting.vvue.place.model.Place;

public interface PlaceRepository {
	List<Place> findByUser_Id(long userId);

	Place getReferenceById(long placeId);

	Optional<Place> findById(Long placeId);

	Place save(Place place);

	void delete(Place place);

	List<Place> findAllById(List<Long> placeIds) ;
}
