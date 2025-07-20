package com.exciting.vvue.place;

public interface FavoritePlaceService {

  boolean checkScrap(long userId, long placeId);

  void addScrap(long userId, long placeId);

  void deleteScrap(long userId, long placeId);

}
