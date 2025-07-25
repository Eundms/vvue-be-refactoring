package com.exciting.vvue.memory;

import com.exciting.vvue.auth.AuthContext;
import com.exciting.vvue.married.MarriedService;
import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.memory.dto.req.MemoryAddReqDto;
import com.exciting.vvue.memory.dto.req.MemoryCreateResDto;
import com.exciting.vvue.memory.dto.res.MemoryAlbumResDto;
import com.exciting.vvue.memory.dto.res.MemoryResDto;
import com.exciting.vvue.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class MemoryController {

  private final MemoryService memoryService;
  private final MarriedService marriedService;

  @Operation(summary = "추억 추가")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "성공"),
      @ApiResponse(responseCode = "400", description = "본인이 작성한 일정별 추억이 이미 존재함"),
  })
  @PostMapping
  public ResponseEntity<MemoryCreateResDto> addMemory(
      @RequestBody @Validated MemoryAddReqDto memoryAddReqDto) {
    Long userId = AuthContext.getUserId();

    Married userMarried = marriedService.getMarriedByUserIdWithDetails(userId);
    User user =
        userMarried.getFirst().getId() == userId ? userMarried.getFirst() : userMarried.getSecond();

    Long memoryId = memoryService.add(memoryAddReqDto, user, userMarried);
    return ResponseEntity.status(HttpStatus.OK).body(new MemoryCreateResDto(memoryId));
  }

  @Operation(summary = "특정 추억 조회", description = "추억ID를 통해 특정 추억 조회")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "성공"),
      @ApiResponse(responseCode = "400", description = "추억ID에 해당하는 추억이 없음"),
  })
  @GetMapping("/{scheduleMemoryId}")//scheduleMemory.id
  public ResponseEntity<MemoryResDto> getMemory(@PathVariable Long scheduleMemoryId) {
    Long userId = AuthContext.getUserId();

    MemoryResDto memoryResDto = memoryService.getById(scheduleMemoryId, userId);
    return ResponseEntity.ok().body(memoryResDto);
  }

  @Operation(summary = "추억 삭제")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "성공"),
      @ApiResponse(responseCode = "400", description = "추억ID 잘못됨"),
  })
  @DeleteMapping("/{memoryId}")
  public ResponseEntity<?> deleteMemory(@PathVariable Long memoryId) {
    Long userId = AuthContext.getUserId();

    memoryService.deleteById(memoryId, userId);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "모든 추억 조회", description = "앨범 썸네일")
  @GetMapping
  public ResponseEntity<MemoryAlbumResDto> getAllMemory(Long nextCursor, int size) { //인스타
    Long userId = AuthContext.getUserId();

    Married married = marriedService.getMarriedByUserIdWithDetails(userId);
    MemoryAlbumResDto memoryAlbumResDto = memoryService.getAllThumbnail(married, nextCursor, size);

    return ResponseEntity.ok().body(memoryAlbumResDto);
  }

}
