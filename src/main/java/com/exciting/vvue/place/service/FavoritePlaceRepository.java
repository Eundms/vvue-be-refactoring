package com.exciting.vvue.place.service;

import java.util.Optional;

import com.exciting.vvue.place.model.FavoritePlace;

public interface FavoritePlaceRepository {
	Optional<FavoritePlace> findByUser_IdAndPlaceId(long userId, long placeId);

	void save(FavoritePlace favoritePlace);

	void delete(FavoritePlace favoritePlace);
}
