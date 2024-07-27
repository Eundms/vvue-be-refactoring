package com.exciting.vvue.notification.model.dto;

import com.exciting.vvue.notification.model.NotificationContent;
import com.exciting.vvue.notification.model.NotificationType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@ToString
@Getter
@NoArgsConstructor
public class NotificationReqDto {
    private Long targetUserId;
    private NotificationType type;
    private NotificationContent content;
    private Map<String, String> data;

    @Builder
    public NotificationReqDto(Long targetUserId, NotificationType type, NotificationContent content, Map<String, String> data) {
        this.targetUserId = targetUserId;
        this.type = type;
        this.content = content;
        this.data = data;
    }
}
