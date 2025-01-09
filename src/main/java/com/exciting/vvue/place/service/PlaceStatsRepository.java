package com.exciting.vvue.place.service;

import java.util.List;
import java.util.Optional;

import com.exciting.vvue.place.model.PlaceStats;
import com.exciting.vvue.place.model.dto.RecommendPlaceResDto;

public interface PlaceStatsRepository {
	List<RecommendPlaceResDto> findRecommendPlacesByLocation(Long userId, double lat, double lng, Long distance,
		Long idCursor, double rateCursor, Long size);

	Optional<PlaceStats> findById(Long cursor);

	void updateSummary();
}
