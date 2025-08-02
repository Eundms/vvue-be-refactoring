package com.exciting.vvue.notification.model;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@ToString
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Entity
public class VvueNotification {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private NotificationType notificationType;

  @Embedded
  private NotificationContent content;

  private Boolean isRead;

  private Long receiverId;

  @CreatedDate
  private LocalDateTime createdAt;

  @Builder
  public VvueNotification(Long id, NotificationContent content, Boolean isRead,
      NotificationType notificationType, Long receiverId, LocalDateTime createdAt) {
    this.id = id;
    this.content = content;
    this.isRead = isRead;
    this.notificationType = notificationType;
    this.receiverId = receiverId;
    this.createdAt = createdAt;
  }
}
