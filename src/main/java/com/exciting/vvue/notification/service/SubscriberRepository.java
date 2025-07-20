package com.exciting.vvue.notification.service;

import com.exciting.vvue.notification.model.Subscriber;
import java.util.List;
import java.util.Optional;

public interface SubscriberRepository {

  Optional<Subscriber> findByUserId(Long userId);

  Optional<Subscriber> findByUserIdAndFirebaseToken(Long userId, String firebaseToken);

  void save(Subscriber subscriber);

  void delete(Subscriber subscriber);

  List<Subscriber> findByUserIdIn(List<Long> receiverIds);

  void saveOrUpdate(Long userId, String firebaseToken);
}
