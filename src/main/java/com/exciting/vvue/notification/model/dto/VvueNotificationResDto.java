package com.exciting.vvue.notification.model.dto;

import com.exciting.vvue.notification.model.NotificationContent;
import com.exciting.vvue.notification.model.NotificationType;
import com.exciting.vvue.notification.model.VvueNotification;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.IOException;
import java.util.Map;

@ToString
@Getter
public class VvueNotificationResDto {
    private Long id;
    private NotificationType notificationType;
    private NotificationContent content;
    private Map<String, String> data;
    private Boolean isRead;
    private Long receiverId;
    private String createdAt;

    @Builder
    public VvueNotificationResDto(Long id, NotificationType notificationType, NotificationContent content, Map<String, String> data, Boolean isRead, Long receiverId, String createdAt) {
        this.id = id;
        this.notificationType = notificationType;
        this.content = content;
        this.data = data;
        this.isRead = isRead;
        this.receiverId = receiverId;
        this.createdAt = createdAt;
    }

    public static VvueNotificationResDto from(VvueNotification vvueNotification) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> item = null;
        String jsonString = vvueNotification.getData();
        if (jsonString != null) {
            try {
                item = objectMapper.readValue(jsonString, new TypeReference<>(){});
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return VvueNotificationResDto.builder()
                .id(vvueNotification.getId())
                .notificationType(vvueNotification.getNotificationType())
                .content(vvueNotification.getContent())
                //.data(item)
                .isRead(vvueNotification.getIsRead())
                .receiverId(vvueNotification.getReceiverId())
                .createdAt(vvueNotification.getCreatedAt().toString())
                .build();
    }
}
