package com.exciting.vvue.place.service;

import com.exciting.vvue.place.exception.PlaceNotFoundException;
import com.exciting.vvue.place.exception.PlaceRequiredException;
import com.exciting.vvue.place.model.Place;
import com.exciting.vvue.place.model.PlaceStats;
import com.exciting.vvue.place.model.dto.PlaceReqDto;
import com.exciting.vvue.place.model.dto.PlaceResDto;
import com.exciting.vvue.place.model.dto.RecommendPlaceListResDto;
import com.exciting.vvue.place.model.dto.RecommendPlaceResDto;
import com.exciting.vvue.place.repository.PlaceRepository;
import com.exciting.vvue.place.repository.PlaceStatsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PlaceServiceImpl implements PlaceService{

    private final PlaceRepository placeRepository;
    private final PlaceStatsRepository placeStatsRepository;

    @Override
    public PlaceResDto getPlace(Long placeId) {
        Place place = placeRepository.findById(placeId).orElseThrow(
                () -> new PlaceNotFoundException("" + placeId)
        );
        PlaceResDto placeResDto = PlaceResDto.from(place);
        return placeResDto;
    }

    // mapPlaceId -> id를 얻기
    @Override
    public Long checkPlace(Long mapPlaceId) {
        Place place = placeRepository.findById(mapPlaceId).orElseThrow(
                () -> new PlaceRequiredException(""+mapPlaceId)
        );
        return place.getId();
    }

    @Override
    public long addPlace(PlaceReqDto placeReqDto) {
        Place place = Place.from(placeReqDto);

        return placeRepository.save(place).getId();
    }

    @Override
    public void deletePlace(long placeId) {
        Place place = placeRepository.findById(placeId).orElseThrow(
                () -> new PlaceNotFoundException("" + placeId)
        );

        placeRepository.delete(place);
    }

    @Override
    public List<PlaceResDto> getScrappedPlaces(long userId) {
        List<PlaceResDto> placeResDtoList = placeRepository.findByUser_Id(userId).stream()
                .map(place -> PlaceResDto.from(place))
                .collect(Collectors.toList());
        return placeResDtoList;
    }

    @Override
    public RecommendPlaceListResDto getRecommendPlaces(Long userId, double lat, double lng, Long distance, Long cursor, Long size){
        PlaceStats placeStats = placeStatsRepository.findById(cursor).orElse(PlaceStats.builder().id(-1L).avgRating(5).build());
        List<RecommendPlaceResDto> recommendPlaceReqDtoList = placeStatsRepository.findRecommendPlacesByLocation(userId, lat, lng, distance * 1000, placeStats.getId(), placeStats.getAvgRating(), size+1);
        boolean hasNext = true;
        if(recommendPlaceReqDtoList.size() <= size) {
            hasNext = false;
        }else{
            if(recommendPlaceReqDtoList.size() > 0)
                recommendPlaceReqDtoList.remove(recommendPlaceReqDtoList.size() - 1);
        }
        String lastId = "";
        if(recommendPlaceReqDtoList.size() > 0)
            lastId = recommendPlaceReqDtoList.get(recommendPlaceReqDtoList.size() - 1).getId();
        RecommendPlaceListResDto recommendPlaceListResDto = RecommendPlaceListResDto.builder().recommendPlaceResDtoList(recommendPlaceReqDtoList).hasNext(hasNext).lastId(lastId).build();

        return recommendPlaceListResDto;
    }
}
