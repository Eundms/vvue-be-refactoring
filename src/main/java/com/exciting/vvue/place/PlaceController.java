package com.exciting.vvue.place;

import com.exciting.vvue.auth.service.AuthService;
import com.exciting.vvue.place.model.dto.PlaceReqDto;
import com.exciting.vvue.place.model.dto.PlaceResDto;
import com.exciting.vvue.place.model.dto.RecommendPlaceListResDto;
import com.exciting.vvue.place.service.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/places")
@RequiredArgsConstructor
public class PlaceController {
    private final PlaceService placeService;
    private final AuthService authService;

    @GetMapping("/{placeId}")
    @Operation(summary ="장소 조회", description = "해당 장소 상세 정보 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공")
            , @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
    })
    public ResponseEntity<PlaceResDto> get(@PathVariable long placeId){
        PlaceResDto placeResDto = placeService.getPlace(placeId);

        return ResponseEntity.ok().body(placeResDto);
    }

    @PostMapping
    @Operation(summary ="장소 등록", description = "장소를 등록한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공")
            , @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
    })
    public ResponseEntity<?> add(@RequestBody PlaceReqDto placeReqDto){
        placeService.addPlace(placeReqDto);

        return ResponseEntity.ok().body("장소 등록 성공");
    }

    @DeleteMapping("/{placeId}")
    @Operation(summary ="장소 삭제", description = "장소를 삭제한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공")
            , @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
            , @ApiResponse(responseCode = "404", description = "해당 장소가 존재하지 않음")
            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
    })
    public ResponseEntity<?> delete(@PathVariable long placeId){
        placeService.deletePlace(placeId);

        return ResponseEntity.ok("장소 삭제 성공");
    }

    @GetMapping("/favorites")
    @Operation(summary ="즐겨찾는 장소 목록 조회", description = "해당 유저의 즐겨찾는 장소 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공")
            , @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
            , @ApiResponse(responseCode = "401", description = "로그인되지 않은  사용자")
            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
    })
    public ResponseEntity<?> getFavoritePlaces(@RequestHeader("Authorization") String token){
        Long userId = authService.getUserIdFromToken(token);

        List<PlaceResDto> placeResDtoList = placeService.getScrappedPlaces(userId);

        return ResponseEntity.ok().body(placeResDtoList);
    }

    @GetMapping("/recommend")
    @Operation(summary="추천 장소 목록 조회", description = "하둡을 통한 추천 장소 별점 순으로 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공")
            , @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
    })
    public ResponseEntity<?> getRecommendPlace(@RequestHeader("Authorization") String token, @RequestParam double lat, @RequestParam double lng, @RequestParam Long distance, @RequestParam Long cursor, @RequestParam Long size){
        Long userId = authService.getUserIdFromToken(token);

        RecommendPlaceListResDto recommendPlaceListResDto = placeService.getRecommendPlaces(userId, lat, lng, distance, cursor, size);

        return ResponseEntity.ok().body(recommendPlaceListResDto);
    }
}
