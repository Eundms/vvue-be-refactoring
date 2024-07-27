package com.exciting.vvue.place.service;

import com.exciting.vvue.place.exception.ScrapNotFoundException;

public interface FavoritePlaceService {
    boolean checkScrap(long userId, long placeId);
    void addScrap(long userId, long placeId);
    void deleteScrap(long userId, long placeId);

}
