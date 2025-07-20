package com.exciting.vvue.place.service;

import com.exciting.vvue.place.dto.res.RecommendPlaceResDto;
import com.exciting.vvue.place.model.PlaceStats;
import java.util.List;
import java.util.Optional;

public interface PlaceStatsRepository {

  List<RecommendPlaceResDto> findRecommendPlacesByLocation(Long userId, double lat, double lng,
      Long distance,
      Long idCursor, double rateCursor, Long size);

  Optional<PlaceStats> findById(Long cursor);

  void updateSummary();
}
