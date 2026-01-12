package com.exciting.vvue.place;

import com.exciting.vvue.place.dto.req.PlaceReqDto;
import com.exciting.vvue.place.dto.res.PlaceResDto;
import com.exciting.vvue.place.dto.res.RecommendPlaceListResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface PlaceApi {

  @Operation(summary = "장소 조회", description = "해당 장소 상세 정보 조회")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청"),
      @ApiResponse(responseCode = "404", description = "해당 장소가 존재하지 않음"),
      @ApiResponse(responseCode = "500", description = "DB 서버 에러")
  })
  PlaceResDto get(@PathVariable long placeId);

  @Operation(summary = "장소 등록", description = "장소를 등록한다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청"),
      @ApiResponse(responseCode = "500", description = "DB 서버 에러")
  })
  String add(@RequestBody PlaceReqDto placeReqDto);

  @Operation(summary = "장소 삭제", description = "장소를 삭제한다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청"),
      @ApiResponse(responseCode = "404", description = "해당 장소가 존재하지 않음"),
      @ApiResponse(responseCode = "500", description = "DB 서버 에러")
  })
  String delete(@PathVariable long placeId);

  @Operation(summary = "즐겨찾는 장소 목록 조회", description = "해당 유저의 즐겨찾는 장소 목록 조회")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자"),
      @ApiResponse(responseCode = "500", description = "DB 서버 에러")
  })
  List<PlaceResDto> getFavoritePlaces();

  @Operation(summary = "추천 장소 목록 조회", description = "하둡을 통한 추천 장소 별점 순으로 조회")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청"),
      @ApiResponse(responseCode = "500", description = "DB 서버 에러")
  })
  RecommendPlaceListResDto getRecommendPlace(
      @RequestParam double lat,
      @RequestParam double lng,
      @RequestParam Long distance,
      @RequestParam Long cursor,
      @RequestParam Long size);
}
