package com.exciting.vvue.place.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exciting.vvue.place.model.FavoritePlace;

@Repository
public interface FavoritePlaceJpaRepository extends JpaRepository<FavoritePlace, Long> {

    Optional<FavoritePlace> findByUser_IdAndPlaceId(long userId, long placeId);

}
