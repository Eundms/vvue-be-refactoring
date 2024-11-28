package com.exciting.vvue.notification.service;

import java.util.Optional;

import com.exciting.vvue.notification.model.Subscriber;

public interface SubscriberRepository { // Redis로 바꾸기!
    Optional<Subscriber> findByUserId(Long userId);

    Optional<Subscriber> findByUserIdAndFirebaseToken(Long userId, String firebaseToken);

    void save(Subscriber subscriber);

    void delete(Subscriber subscriber);
}
