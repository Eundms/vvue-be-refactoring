package com.exciting.vvue.place;

import com.exciting.vvue.auth.AuthContext;
import com.exciting.vvue.place.dto.req.PlaceReqDto;
import com.exciting.vvue.place.dto.res.PlaceResDto;
import com.exciting.vvue.place.dto.res.RecommendPlaceListResDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
  public String add(@RequestBody PlaceReqDto placeReqDto) {
    placeService.addPlace(placeReqDto);
    return "장소 등록 성공";
  }

  @DeleteMapping("/{placeId}")
  @Override
  public String delete(@PathVariable long placeId) {
    placeService.deletePlace(placeId);
    return "장소 삭제 성공";
  }

  @GetMapping("/favorites")
  @Override
  public List<PlaceResDto> getFavoritePlaces() {
    Long userId = AuthContext.getUserId();
    return placeService.getScrappedPlaces(userId);
  }

  @GetMapping("/recommend")
  @Override
  public RecommendPlaceListResDto getRecommendPlace(@RequestParam double lat,
      @RequestParam double lng,
      @RequestParam Long distance, @RequestParam Long cursor, @RequestParam Long size) {
    Long userId = AuthContext.getUserId();
    return placeService.getRecommendPlaces(userId, lat, lng, distance, cursor, size);
  }
}
