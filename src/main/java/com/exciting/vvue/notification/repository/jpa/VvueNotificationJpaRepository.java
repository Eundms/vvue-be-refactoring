package com.exciting.vvue.notification.repository.jpa;

import com.exciting.vvue.notification.model.VvueNotification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VvueNotificationJpaRepository extends JpaRepository<VvueNotification, Long> {

  @Query("select vn from VvueNotification vn where vn.receiverId=:userId")
  List<VvueNotification> findByReceiverId(Long userId);

  @Query("select count(distinct vn.id) from VvueNotification vn where vn.receiverId=:userId and vn.isRead = false")
  int countUnReadByReceiverId(Long userId);

  @Modifying(clearAutomatically = true)
  @Query("update VvueNotification vn set vn.isRead = true where vn.receiverId=:userId and vn.isRead = false")
  void readAllUnReadNotify(Long userId);

  @Modifying(clearAutomatically = true)
  @Query("update VvueNotification vn set vn.isRead = true where vn.id=:notificationId and vn.receiverId=:userId")
  void readUnReadNotify(Long userId, Long notificationId);
}
