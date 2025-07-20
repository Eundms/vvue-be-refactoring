package com.exciting.vvue.place.service;

import com.exciting.vvue.place.model.FavoritePlace;
import java.util.Optional;

public interface FavoritePlaceRepository {

  Optional<FavoritePlace> findByUser_IdAndPlaceId(long userId, long placeId);

  void save(FavoritePlace favoritePlace);

  void delete(FavoritePlace favoritePlace);
}
