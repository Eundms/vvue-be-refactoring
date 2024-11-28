package com.exciting.vvue.notification.service;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.exciting.vvue.notification.model.VvueNotification;

@Repository
public interface VvueNotificationRepository {
    List<VvueNotification> findByReceiverId(Long userId);

    int countUnReadByReceiverId(Long userId);
    void readAllUnReadNotify(Long userId);

    void readUnReadNotify(Long userId, Long notificationId);

    void save(VvueNotification vvueNotification);
}
