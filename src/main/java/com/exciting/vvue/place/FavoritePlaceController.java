package com.exciting.vvue.place;

import com.exciting.vvue.auth.AuthContext;
import com.exciting.vvue.place.dto.req.PlaceReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/places-favorite")
@RequiredArgsConstructor
public class FavoritePlaceController implements FavoritePlaceApi {

  private final FavoritePlaceService favoritePlaceService;
  private final PlaceService placeService;

  @PostMapping
  @Override
  public boolean changeScrap(@RequestParam Long mapPlaceId) {
    Long userId = AuthContext.getUserId();
    Long placeId = placeService.checkPlace(mapPlaceId);

    if (favoritePlaceService.checkScrap(userId, placeId)) {
      favoritePlaceService.deleteScrap(userId, placeId);
      return false;
    } else {
      favoritePlaceService.addScrap(userId, placeId);
      return true;
    }
  }

  @PostMapping("/regist")
  @Override
  public String scrapAndAdd(@RequestBody PlaceReqDto placeReqDto) {
    Long userId = AuthContext.getUserId();
    long placeId = placeService.addPlace(placeReqDto);
    favoritePlaceService.addScrap(userId, placeId);
    return "장소 등록 및 즐겨찾기 성공";
  }
}
