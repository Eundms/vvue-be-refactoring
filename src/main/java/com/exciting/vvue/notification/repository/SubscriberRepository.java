package com.exciting.vvue.notification.repository;

import com.exciting.vvue.notification.model.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber,Long> { // Redis로 바꾸기!
    @Query("select sb from Subscriber sb where sb.userId=:userId")
    Optional<Subscriber> findByUserId(Long userId);

    @Query("select sb from Subscriber sb where sb.userId=:userId and sb.firebaseToken=:firebaseToken")
    Optional<Subscriber> findByUserIdAndFirebaseToken(Long userId, String firebaseToken);
}
