package com.exciting.vvue.place.repository.jpa;

import com.exciting.vvue.place.dto.res.RecommendPlaceResDto;
import com.exciting.vvue.place.model.PlaceStats;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PlaceStatsJpaRepository extends JpaRepository<PlaceStats, Long> {

  @Query(name = "find_recommend_place_res_dto", nativeQuery = true)
  List<RecommendPlaceResDto> findRecommendPlacesByLocation(@Param("userId") Long userId,
      @Param("lat") double lat,
      @Param("lng") double lng, @Param("distance") Long distance, @Param("idCursor") Long idCursor,
      @Param("rateCursor") double rateCursor, @Param("size") Long size);

  @Transactional
  @Modifying
  @Query(value = "INSERT INTO place_stats (id, avg_rating, visit_count)\n"
      + "SELECT \n"
      + "    place_id AS id, \n"
      + "    AVG(rating) AS avg_rating, \n"
      + "    COUNT(id) AS visit_count\n"
      + "FROM \n"
      + "    placememory\n"
      + "GROUP BY \n"
      + "    place_id\n"
      + "ON DUPLICATE KEY UPDATE\n"
      + "    avg_rating = avg_rating, \n"
      + "    visit_count = visit_count", nativeQuery = true)
  void updateSummary();
}
