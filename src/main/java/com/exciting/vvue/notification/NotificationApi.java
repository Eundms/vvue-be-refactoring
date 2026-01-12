package com.exciting.vvue.notification;

import com.exciting.vvue.notification.dto.NotReadNotificationDto;
import com.exciting.vvue.notification.dto.SubscribeReqDto;
import com.exciting.vvue.notification.dto.UnsubscribeResDto;
import com.exciting.vvue.notification.dto.VvueNotificationListDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface NotificationApi {

  @Operation(summary = "알림 목록 조회", description = "nextCursor 초기 : -1, size 0이상")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
  })
  VvueNotificationListDto getAllNotification(Long nextCursor, int size);

  @Operation(summary = "안 읽은 알림 존재 여부 조회", description = "안 읽은 알림 개수(0-N) 리턴")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
  })
  NotReadNotificationDto notReadAlarmExist();

  @Operation(summary = "알림 구독하기", description = "Firebase 토큰으로 알림 구독")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "400", description = "firebaseToken이 null"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
  })
  void subscribe(@RequestBody SubscribeReqDto subscribeReqDto);

  @Operation(summary = "알림 구독 취소", description = "알림 구독을 취소한다")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "204", description = "취소할 구독이 없음"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
  })
  UnsubscribeResDto unsubscribe(@RequestBody SubscribeReqDto subscribeReqDto);

  @Operation(summary = "안 읽은 알림 한번에 다 읽음 처리", description = "모든 알림을 읽음 처리")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
  })
  void allReadUnReadNotify();

  @Operation(summary = "안 읽은 알림 하나 읽음 처리", description = "특정 알림을 읽음 처리")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자"),
      @ApiResponse(responseCode = "404", description = "알림을 찾을 수 없음")
  })
  void readUnReadNotify(@PathVariable Long notificationId);
}
