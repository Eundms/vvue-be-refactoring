package com.exciting.vvue.place;

import com.exciting.vvue.place.dto.req.PlaceReqDto;
import com.exciting.vvue.place.dto.res.PlaceResDto;
import com.exciting.vvue.place.dto.res.RecommendPlaceListResDto;
import java.util.List;

public interface PlaceService {

  PlaceResDto getPlace(Long placeId);

  Long checkPlace(Long placeId);

  long addPlace(PlaceReqDto placeReqDto);

  void deletePlace(long placeId);

  List<PlaceResDto> getScrappedPlaces(long userId);

  RecommendPlaceListResDto getRecommendPlaces(Long userId, double lat, double lng, Long distance,
      Long cursor,
      Long size);
}
