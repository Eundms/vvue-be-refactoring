package com.exciting.vvue.place.repository;

import com.exciting.vvue.place.service.PlaceRepository;
import com.exciting.vvue.place.model.Place;
import com.exciting.vvue.place.repository.jpa.PlaceJpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PlaceRepositoryImpl implements PlaceRepository {
    private final PlaceJpaRepository placeJpaRepository;
    @Override
    public List<Place> findByUser_Id(long userId) {
        return placeJpaRepository.findByUser_Id(userId);
    }

    @Override
    public Place getReferenceById(long placeId) {
        return placeJpaRepository.getReferenceById(placeId);
    }

    @Override
    public Optional<Place> findById(Long placeId) {
        return placeJpaRepository.findById(placeId);
    }

    @Override
    public Place save(Place place) {
        return placeJpaRepository.save(place);
    }

    @Override
    public void delete(Place place) {
        placeJpaRepository.delete(place);
    }
}