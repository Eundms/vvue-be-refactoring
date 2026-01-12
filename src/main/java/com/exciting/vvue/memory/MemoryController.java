package com.exciting.vvue.memory;

import com.exciting.vvue.auth.AuthContext;
import com.exciting.vvue.married.MarriedService;
import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.memory.dto.req.MemoryAddReqDto;
import com.exciting.vvue.memory.dto.req.MemoryCreateResDto;
import com.exciting.vvue.memory.dto.res.MemoryAlbumResDto;
import com.exciting.vvue.memory.dto.res.MemoryResDto;
import com.exciting.vvue.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/memory")
@RequiredArgsConstructor
public class MemoryController implements MemoryApi {

  private final MemoryService memoryService;
  private final MarriedService marriedService;

  @PostMapping
  @Override
  public MemoryCreateResDto addMemory(@RequestBody @Validated MemoryAddReqDto memoryAddReqDto) {
    Long userId = AuthContext.getUserId();

    Married userMarried = marriedService.getMarriedByUserIdWithDetails(userId);
    User user =
        userMarried.getFirst().getId().equals(userId) ? userMarried.getFirst() : userMarried.getSecond();

    Long memoryId = memoryService.add(memoryAddReqDto, user, userMarried);
    return new MemoryCreateResDto(memoryId);
  }

  @GetMapping("/{scheduleMemoryId}")
  @Override
  public MemoryResDto getMemory(@PathVariable Long scheduleMemoryId) {
    Long userId = AuthContext.getUserId();
    return memoryService.getById(scheduleMemoryId, userId);
  }

  @DeleteMapping("/{memoryId}")
  @Override
  public void deleteMemory(@PathVariable Long memoryId) {
    Long userId = AuthContext.getUserId();
    memoryService.deleteById(memoryId, userId);
  }

  @GetMapping
  @Override
  public MemoryAlbumResDto getAllMemory(Long nextCursor, int size) {
    Long userId = AuthContext.getUserId();
    Married married = marriedService.getMarriedByUserIdWithDetails(userId);
    return memoryService.getAllThumbnail(married, nextCursor, size);
  }
}
