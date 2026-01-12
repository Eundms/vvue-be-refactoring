package com.exciting.vvue.place;

import com.exciting.vvue.auth.AuthContext;
import com.exciting.vvue.common.dto.MessageResDto;
import com.exciting.vvue.place.dto.req.PlaceReqDto;
import com.exciting.vvue.place.dto.req.RecommendPlaceSearchReqDto;
import com.exciting.vvue.place.dto.res.FavoritePlaceListResDto;
import com.exciting.vvue.place.dto.res.PlaceResDto;
import com.exciting.vvue.place.dto.res.RecommendPlaceListResDto;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/places")
@RequiredArgsConstructor
public class PlaceController implements PlaceApi {

  private final PlaceService placeService;

  @GetMapping("/{placeId}")
  @Override
  public PlaceResDto get(@PathVariable long placeId) {
    return placeService.getPlace(placeId);
  }

  @PostMapping
  @Override
  public MessageResDto add(@RequestBody PlaceReqDto placeReqDto) {
    placeService.addPlace(placeReqDto);
    return MessageResDto.of("장소 등록 성공");
  }

  @DeleteMapping("/{placeId}")
  @Override
  public MessageResDto delete(@PathVariable long placeId) {
    placeService.deletePlace(placeId);
    return MessageResDto.of("장소 삭제 성공");
  }

  @GetMapping("/favorites")
  @Override
  public FavoritePlaceListResDto getFavoritePlaces() {
    Long userId = AuthContext.getUserId();
    List<PlaceResDto> places = placeService.getScrappedPlaces(userId);
    return FavoritePlaceListResDto.of(places);
  }

  @GetMapping("/recommend")
  @Override
  public RecommendPlaceListResDto getRecommendPlace(
      @Valid @ModelAttribute RecommendPlaceSearchReqDto searchReqDto) {
    Long userId = AuthContext.getUserId();
    return placeService.getRecommendPlaces(
        userId,
        searchReqDto.getLat(),
        searchReqDto.getLng(),
        searchReqDto.getDistance(),
        searchReqDto.getCursor(),
        searchReqDto.getSize());
  }
}
