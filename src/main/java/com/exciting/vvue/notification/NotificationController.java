package com.exciting.vvue.notification;

import com.exciting.vvue.auth.AuthContext;
import com.exciting.vvue.common.exception.ErrorCode;
import com.exciting.vvue.common.exception.VvueException;
import com.exciting.vvue.notification.dto.NotReadNotificationDto;
import com.exciting.vvue.notification.dto.SubscribeReqDto;
import com.exciting.vvue.notification.dto.VvueNotificationListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class NotificationController implements NotificationApi {

  private final NotificationService notificationService;

  @GetMapping
  @Override
  public VvueNotificationListDto getAllNotification(Long nextCursor, int size) {
    Long userId = AuthContext.getUserId();
    return notificationService.getAllNotificationBy(userId, nextCursor, size);
  }

  @GetMapping("/not-read")
  @Override
  public NotReadNotificationDto notReadAlarmExist() {
    Long userId = AuthContext.getUserId();
    return notificationService.getUnReadNotificationBy(userId);
  }

  @PostMapping("/subscribe")
  @Override
  public void subscribe(@RequestBody SubscribeReqDto subscribeReqDto) {
    if (subscribeReqDto == null || subscribeReqDto.getFirebaseToken() == null
        || subscribeReqDto.getFirebaseToken().isBlank()) {
      throw new VvueException(ErrorCode.BAD_REQUEST);
    }
    Long userId = AuthContext.getUserId();
    notificationService.subscribe(userId, subscribeReqDto.getFirebaseToken());
  }

  @PostMapping("/unsubscribe")
  @Override
  public boolean unsubscribe(@RequestBody SubscribeReqDto subscribeReqDto) {
    Long userId = AuthContext.getUserId();
    return notificationService.unsubscribe(userId, subscribeReqDto.getFirebaseToken());
  }

  @GetMapping("/all-read")
  @Override
  public void allReadUnReadNotify() {
    Long userId = AuthContext.getUserId();
    notificationService.readAllUnReadNotify(userId);
  }

  @GetMapping("/read/{notificationId}")
  @Override
  public void readUnReadNotify(@PathVariable Long notificationId) {
    Long userId = AuthContext.getUserId();
    notificationService.readUnReadNotify(userId, notificationId);
  }
}
