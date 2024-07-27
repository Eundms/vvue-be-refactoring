package com.exciting.vvue.place.repository;

import com.exciting.vvue.place.model.FavoritePlace;
import com.exciting.vvue.place.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritePlaceRepository  extends JpaRepository<FavoritePlace, Long> {

    Optional<FavoritePlace> findByUser_IdAndPlaceId(long userId, long placeId);

}
