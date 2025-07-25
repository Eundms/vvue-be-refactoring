package com.exciting.vvue.notification;

import com.exciting.vvue.auth.AuthContext;
import com.exciting.vvue.notification.dto.NotReadNotificationDto;
import com.exciting.vvue.notification.dto.SubscribeReqDto;
import com.exciting.vvue.notification.dto.VvueNotificationListDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/notify")
@RestController
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;

  @Operation(summary = "알림 목록 조회", description = "nextCursor 초기 : -1, size 0이상")
  @GetMapping
  public ResponseEntity<?> getAllNotification(Long nextCursor, int size) {
    Long userId = AuthContext.getUserId();
    VvueNotificationListDto vvueNotifications = notificationService.getAllNotificationBy(userId,
        nextCursor, size);
    return ResponseEntity.status(HttpStatus.OK).body(vvueNotifications);
  }

  @Operation(summary = "안 읽은 알림 존재 여부 조회", description = "안 읽은 알림 개수(0-N) 리턴")
  @GetMapping("/not-read")
  public ResponseEntity<?> notReadAlarmExist() {
    Long userId = AuthContext.getUserId();
    NotReadNotificationDto notReadNotificationDto = notificationService.getUnReadNotificationBy(
        userId);
    return ResponseEntity.status(HttpStatus.OK).body(notReadNotificationDto);
  }

  @Operation(summary = "알림 구독하기")
  @PostMapping("/subscribe")
  public ResponseEntity<?> subscribe(@RequestBody SubscribeReqDto subscribeReqDto) {
    if (subscribeReqDto == null || subscribeReqDto.getFirebaseToken() == null
        || subscribeReqDto.getFirebaseToken()
        .isBlank()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("firebaseToken == null");
    }
    Long userId = AuthContext.getUserId();
    notificationService.subscribe(userId, subscribeReqDto.getFirebaseToken());
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @Operation(summary = "알림 구독 취소")
  @PostMapping("/unsubscribe")
  public ResponseEntity<?> unsubscribe(@RequestBody SubscribeReqDto subscribeReqDto) {

    Long userId = AuthContext.getUserId();
    boolean isSuccess = notificationService.unsubscribe(userId, subscribeReqDto.getFirebaseToken());
    if (isSuccess) {
      return ResponseEntity.status(HttpStatus.OK).build();
    }
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @Operation(summary = "안 읽은 알림 한번에 다 읽음 처리")
  @GetMapping("/all-read")
  public ResponseEntity<?> allReadUnReadNotify() {
    Long userId = AuthContext.getUserId();
    notificationService.readAllUnReadNotify(userId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @Operation(summary = "안 읽은 알림 하나 읽음 처리")
  @GetMapping("/read/{notificationId}")
  public ResponseEntity<?> readUnReadNotify(@PathVariable Long notificationId) {
    Long userId = AuthContext.getUserId();
    notificationService.readUnReadNotify(userId, notificationId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
