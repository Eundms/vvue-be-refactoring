package com.exciting.vvue.notification;

import com.exciting.vvue.auth.service.AuthService;
import com.exciting.vvue.notification.model.dto.NotReadNotificationDto;
import com.exciting.vvue.notification.model.dto.NotificationReqDto;
import com.exciting.vvue.notification.model.dto.SubscribeReqDto;
import com.exciting.vvue.notification.model.dto.VvueNotificationListDto;
import com.exciting.vvue.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RequestMapping("/notify")
@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final AuthService authService;

    @Operation(summary ="알림 목록 조회", description="nextCursor 초기 : -1, size 0이상")
    @GetMapping
    public ResponseEntity<?> getAllNotification(@RequestHeader("Authorization") String token, Long nextCursor, int size) {
        Long userId = authService.getUserIdFromToken(token);
        log.debug("[GET] /notify : userId("+userId+")");
        VvueNotificationListDto vvueNotifications = notificationService.getAllNotificationBy(userId,nextCursor,size);
        return ResponseEntity.status(HttpStatus.OK).body(vvueNotifications);
    }

    @Operation(summary ="안 읽은 알림 존재 여부 조회",description = "안 읽은 알림 개수(0-N) 리턴")
    @GetMapping("/not-read")
    public ResponseEntity<?> notReadAlarmExist(@RequestHeader("Authorization") String token) {
        Long userId = authService.getUserIdFromToken(token);
        NotReadNotificationDto notReadNotificationDto = notificationService.getUnReadNotificationBy(userId);
        return ResponseEntity.status(HttpStatus.OK).body(notReadNotificationDto);
    }

    @Operation(summary ="알림 구독하기")
    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(@RequestHeader("Authorization") String token, @RequestBody SubscribeReqDto subscribeReqDto) {
        Long userId = authService.getUserIdFromToken(token);
        if(subscribeReqDto==null || subscribeReqDto.getFirebaseToken()==null || subscribeReqDto.getFirebaseToken().isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("firebaseToken == null");
        }

        notificationService.subscribe(userId, subscribeReqDto.getFirebaseToken());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary ="알림 구독 취소")
    @PostMapping("/unsubscribe")
    public ResponseEntity<?> unsubscribe(@RequestHeader("Authorization") String token, @RequestBody SubscribeReqDto subscribeReqDto) {
        Long userId = authService.getUserIdFromToken(token);
        boolean isSuccess = notificationService.unsubscribe(userId, subscribeReqDto.getFirebaseToken());
        if(isSuccess){
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary ="알림을 특정 유저에게 보내기")
    @PostMapping("/users")
    public ResponseEntity<?> notifyToUser(@RequestBody NotificationReqDto notificationReqDto) {
        notificationService.sendByToken(notificationReqDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary ="알림을 리스트 유저에게 보내기(bulk)")
    @PostMapping("/users/all")
    public ResponseEntity<?> notifyToAllUser(@RequestBody List<NotificationReqDto> notificationReqDtos) {
        notificationReqDtos.stream()
                .forEach(notificationService::sendByToken);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary="안 읽은 알림 한번에 다 읽음 처리")
    @GetMapping("/all-read")
    public ResponseEntity<?> allReadUnReadNotify(@RequestHeader("Authorization") String token){
        Long userId = authService.getUserIdFromToken(token);
        notificationService.readAllUnReadNotify(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary="안 읽은 알림 하나 읽음 처리")
    @GetMapping("/read/{notificationId}")
    public ResponseEntity<?> readUnReadNotify(@RequestHeader("Authorization") String token,@PathVariable Long notificationId){
        Long userId = authService.getUserIdFromToken(token);
        notificationService.readUnReadNotify(userId, notificationId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
