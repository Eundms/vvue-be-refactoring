package com.exciting.vvue.place.repository;

import com.exciting.vvue.place.model.PlaceStats;
import com.exciting.vvue.place.model.dto.RecommendPlaceResDto;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceStatsRepository extends JpaRepository<PlaceStats, Long> {
    @Query(name = "find_recommend_place_res_dto", nativeQuery = true)
    List<RecommendPlaceResDto> findRecommendPlacesByLocation(@Param("userId") Long userId, @Param("lat") double lat, @Param("lng") double lng, @Param("distance") Long distance, @Param("idCursor") Long idCursor, @Param("rateCursor") double rateCursor, @Param("size") Long size);
}
