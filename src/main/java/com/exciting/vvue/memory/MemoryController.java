package com.exciting.vvue.memory;

import com.exciting.vvue.auth.service.AuthService;
import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.married.service.MarriedService;
import com.exciting.vvue.memory.model.dto.req.MemoryAddReqDto;
import com.exciting.vvue.memory.model.dto.req.MemoryCreateResDto;
import com.exciting.vvue.memory.model.dto.res.MemoryAlbumResDto;
import com.exciting.vvue.memory.model.dto.res.MemoryResDto;
import com.exciting.vvue.memory.service.MemoryService;

import com.exciting.vvue.notification.service.NotificationService;
import com.exciting.vvue.schedule.service.ScheduleService;
import com.exciting.vvue.user.model.User;
import com.exciting.vvue.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/memory")
@RequiredArgsConstructor
public class MemoryController {
    private final MemoryService memoryService;
    private final AuthService authService;
    private final UserService userService;
    private final MarriedService marriedService;

    private final ScheduleService scheduleService;
    private final NotificationService notificationService;

    @Operation(summary ="추억 추가")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "본인이 작성한 일정별 추억이 이미 존재함"),
    })
    @PostMapping
    public ResponseEntity<?> addMemory(@RequestHeader("Authorization") String token, @RequestBody @Validated MemoryAddReqDto memoryAddReqDto) {
        log.debug("[POST] /memory : " + memoryAddReqDto);
        Long userId = authService.getUserIdFromToken(token);
        User user = userService.getUserById(userId);
        Married userMarried = marriedService.getMarriedByUserId(userId);
        log.debug("[userId={}]가 포함된 " + userMarried, userId);
        Long memoryId = memoryService.add(memoryAddReqDto, user, userMarried);

        // 알림 요청
//        ScheduleResDto schedule = scheduleService.getSchedule(memoryAddReqDto.getScheduleId());
//        Long spouseId = marriedService.getSpouseIdById(userId);
//        User spouse = userService.getUserById(spouseId);
//        Map<String, String> data = new HashMap<>();
//        data.put("memoryId", Long.toString(memoryId));
//        notificationService.sendByToken(
//                NotificationReqDto.builder()
//                        .targetUserId(spouseId)
//                        .content(
//                                NotificationContent.builder()
//                                        .title("추억 추가")
//                                        .body("배우자가 \"" + schedule.getScheduleName() + " " + schedule.getCurDate() + "\"에 대한 추억을 등록했어요")
//                                        .image(spouse.getPicture()!=null?spouse.getPicture().getUrl():null)
//                                        .build()
//                        )
//                        .type(NotificationType.MEMORY)
//                        //.data(data)
//                        .build());
        return ResponseEntity.status(HttpStatus.OK).body(new MemoryCreateResDto(memoryId));
    }

    @Operation(summary = "특정 추억 조회", description = "추억ID를 통해 특정 추억 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "추억ID에 해당하는 추억이 없음"),
    })
    @GetMapping("/{scheduleMemoryId}")//scheduleMemory.id
    public ResponseEntity<?> getMemory(@RequestHeader("Authorization") String token, @PathVariable Long scheduleMemoryId) {
        log.debug("[GET] /memory/{scheduleMemoryId} : " + scheduleMemoryId);
        Long userId = authService.getUserIdFromToken(token);
        User user = userService.getUserById(userId);

        MemoryResDto memoryResDto = memoryService.getById(scheduleMemoryId, user);
        return ResponseEntity.ok().body(memoryResDto);
    }
    @Operation(summary = "[TODO] 추억 수정", description = "추억ID를 통해 특정 추억 수정")
    @PutMapping("/{memoryId}") //TODO
    public ResponseEntity<?> editMemory(@PathVariable Long memoryId) {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "추억 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "추억ID 잘못됨"),
    })
    @DeleteMapping("/{memoryId}")
    public ResponseEntity<?> deleteMemory(@RequestHeader("Authorization") String token, @PathVariable Long memoryId) {
        Long userId = authService.getUserIdFromToken(token);
        User user = userService.getUserById(userId);

        memoryService.deleteById(memoryId, user);
        return ResponseEntity.ok().build();
    }
    @Operation(summary ="모든 추억 조회", description = "앨범 썸네일")
    @GetMapping
    public ResponseEntity<?> getAllMemory(@RequestHeader("Authorization") String token, Long nextCursor, int size) { //인스타
        Long userId = authService.getUserIdFromToken(token);
        User user = userService.getUserById(userId);
        Married married = marriedService.getMarriedByUserId(userId);
        MemoryAlbumResDto memoryAlbumResDto = memoryService.getAllThumbnail(married, nextCursor, size);

        return ResponseEntity.ok().body(memoryAlbumResDto);
    }


}
