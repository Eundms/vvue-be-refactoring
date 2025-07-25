package com.exciting.vvue.memory;

import com.exciting.vvue.auth.AuthContext;
import com.exciting.vvue.memory.dto.MemoryPlaceFindDto;
import com.exciting.vvue.memory.dto.res.PlaceMemoryToMapDto;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/memory-place")
@RequiredArgsConstructor
public class MemoryPlaceController {

  private final MemoryPlaceService memoryPlaceService;

  @Operation(summary = "추억 지도 조회")
  @GetMapping
  public ResponseEntity<?> searchMemoryPlace(
      @RequestParam(required = false) MemoryPlaceFindDto findCondition) {
    Long userId = AuthContext.getUserId();
    List<PlaceMemoryToMapDto> placeMemories = memoryPlaceService.getMemoriablePlaceByMarriedId(
        userId, findCondition);
    return ResponseEntity.ok().body(placeMemories);
  }

  @Deprecated
  @Operation(summary = "[TODO] (특정 장소의) 모든 추억 조회")
  @GetMapping("/{placeId}")
  public ResponseEntity<?> searchMemoryPlace(@PathVariable Long placeId) {
    return ResponseEntity.ok().build();
  }
}
