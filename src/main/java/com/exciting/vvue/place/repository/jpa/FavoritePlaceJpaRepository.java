package com.exciting.vvue.place.repository.jpa;

import com.exciting.vvue.place.model.FavoritePlace;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoritePlaceJpaRepository extends JpaRepository<FavoritePlace, Long> {

  Optional<FavoritePlace> findByUser_IdAndPlaceId(long userId, long placeId);

}
