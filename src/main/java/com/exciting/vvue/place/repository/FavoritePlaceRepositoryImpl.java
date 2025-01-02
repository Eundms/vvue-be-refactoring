package com.exciting.vvue.place.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.exciting.vvue.place.model.FavoritePlace;
import com.exciting.vvue.place.repository.jpa.FavoritePlaceJpaRepository;
import com.exciting.vvue.place.service.FavoritePlaceRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FavoritePlaceRepositoryImpl implements FavoritePlaceRepository {
	private final FavoritePlaceJpaRepository favoritePlaceJpaRepository;

	@Override
	public Optional<FavoritePlace> findByUser_IdAndPlaceId(long userId, long placeId) {
		return favoritePlaceJpaRepository.findByUser_IdAndPlaceId(userId, placeId);
	}

	@Override
	public void save(FavoritePlace favoritePlace) {
		favoritePlaceJpaRepository.save(favoritePlace);
	}

	@Override
	public void delete(FavoritePlace favoritePlace) {
		favoritePlaceJpaRepository.delete(favoritePlace);
	}
}
