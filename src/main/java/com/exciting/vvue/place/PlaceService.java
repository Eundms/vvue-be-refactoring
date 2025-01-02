package com.exciting.vvue.place;

import java.util.List;

import com.exciting.vvue.place.model.dto.PlaceReqDto;
import com.exciting.vvue.place.model.dto.PlaceResDto;
import com.exciting.vvue.place.model.dto.RecommendPlaceListResDto;

public interface PlaceService {
	PlaceResDto getPlace(Long placeId);

	Long checkPlace(Long placeId);

	long addPlace(PlaceReqDto placeReqDto);

	void deletePlace(long placeId);

	List<PlaceResDto> getScrappedPlaces(long userId);

	RecommendPlaceListResDto getRecommendPlaces(Long userId, double lat, double lng, Long distance, Long cursor,
		Long size);
}
