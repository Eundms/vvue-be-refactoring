package com.exciting.vvue.notification.repository;

import com.exciting.vvue.notification.service.SubscriberRepository;
import com.exciting.vvue.notification.model.Subscriber;
import com.exciting.vvue.notification.repository.jpa.SubscriberJpaRepository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SubscriberRepositoryImpl implements SubscriberRepository {

    private final SubscriberJpaRepository subscriberJpaRepository;
    @Override
    public Optional<Subscriber> findByUserId(Long userId) {
        return subscriberJpaRepository.findByUserId(userId);
    }

    @Override
    public Optional<Subscriber> findByUserIdAndFirebaseToken(Long userId, String firebaseToken) {
        return subscriberJpaRepository.findByUserIdAndFirebaseToken(userId, firebaseToken);
    }

    @Override
    public void save(Subscriber subscriber) {
        subscriberJpaRepository.save(subscriber);
    }

    @Override
    public void delete(Subscriber subscriber) {
        subscriberJpaRepository.delete(subscriber);
    }
}
