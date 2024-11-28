package com.exciting.vvue.place.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.exciting.vvue.place.service.PlaceStatsRepository;
import com.exciting.vvue.place.model.PlaceStats;
import com.exciting.vvue.place.model.dto.RecommendPlaceResDto;
import com.exciting.vvue.place.repository.jpa.PlaceStatsJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PlaceStatsRepositoryImpl implements PlaceStatsRepository {
	private final PlaceStatsJpaRepository placeStatsJpaRepository;
	@Override
	public List<RecommendPlaceResDto> findRecommendPlacesByLocation(Long userId, double lat, double lng, Long distance,
		Long idCursor, double rateCursor, Long size) {
		return placeStatsJpaRepository.findRecommendPlacesByLocation(userId, lat, lng, distance, idCursor, rateCursor,size);
	}

	@Override
	public Optional<PlaceStats> findById(Long cursor) {
		return placeStatsJpaRepository.findById(cursor);
	}
}
