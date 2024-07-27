package com.exciting.vvue.place.repository;

import com.exciting.vvue.place.model.Place;
import com.exciting.vvue.place.model.dto.PlaceResDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    @Query("select p from FavoritePlace f left join Place p on f.user.id=:userId and f.place.id = p.id")
    List<Place> findByUser_Id(long userId);

}