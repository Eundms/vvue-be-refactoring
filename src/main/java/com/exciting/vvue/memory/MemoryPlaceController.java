package com.exciting.vvue.memory;

import com.exciting.vvue.auth.AuthContext;
import com.exciting.vvue.memory.dto.MemoryPlaceFindDto;
import com.exciting.vvue.memory.dto.res.PlaceMemoryToMapDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/memory-place")
@RequiredArgsConstructor
public class MemoryPlaceController implements MemoryPlaceApi {

  private final MemoryPlaceService memoryPlaceService;

  @GetMapping
  @Override
  public List<PlaceMemoryToMapDto> searchMemoryPlace(
      @RequestParam(required = false) MemoryPlaceFindDto findCondition) {
    Long userId = AuthContext.getUserId();
    return memoryPlaceService.getMemoriablePlaceByMarriedId(userId, findCondition);
  }

  @Deprecated
  @GetMapping("/{placeId}")
  @Override
  public void searchMemoryPlace(@PathVariable Long placeId) {
    // TODO: implement
  }
}
