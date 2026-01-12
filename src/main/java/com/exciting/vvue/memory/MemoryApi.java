package com.exciting.vvue.memory;

import com.exciting.vvue.memory.dto.req.MemoryAddReqDto;
import com.exciting.vvue.memory.dto.req.MemoryCreateResDto;
import com.exciting.vvue.memory.dto.res.MemoryAlbumResDto;
import com.exciting.vvue.memory.dto.res.MemoryResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface MemoryApi {

  @Operation(summary = "추억 추가", description = "새 추억을 등록한다")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "성공"),
      @ApiResponse(responseCode = "400", description = "본인이 작성한 일정별 추억이 이미 존재함"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
  })
  MemoryCreateResDto addMemory(@RequestBody @Validated MemoryAddReqDto memoryAddReqDto);

  @Operation(summary = "특정 추억 조회", description = "추억ID를 통해 특정 추억 조회")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "성공"),
      @ApiResponse(responseCode = "400", description = "추억ID에 해당하는 추억이 없음"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
  })
  MemoryResDto getMemory(@PathVariable Long scheduleMemoryId);

  @Operation(summary = "추억 삭제", description = "추억을 삭제한다")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "성공"),
      @ApiResponse(responseCode = "400", description = "추억ID 잘못됨"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
  })
  void deleteMemory(@PathVariable Long memoryId);

  @Operation(summary = "모든 추억 조회", description = "앨범 썸네일")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "성공"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
  })
  MemoryAlbumResDto getAllMemory(Long nextCursor, int size);
}
