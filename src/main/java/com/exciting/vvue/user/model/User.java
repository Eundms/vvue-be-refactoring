package com.exciting.vvue.user.model;

import com.exciting.vvue.picture.model.Picture;
import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@ToString
@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Table(
    name = "USER",
    indexes = {
        @Index(name = "idx_nickname", columnList = "nickname")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_provider_providerId", columnNames = {"provider",
            "provider_id"})
    }
)
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "nickname")
  private String nickname;
  private String email;

  @Column(name = "provider")
  private String provider;

  @Column(name = "provider_id")
  private String providerId;

  @Enumerated(value = EnumType.STRING)
  private Gender gender;
  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  @JoinColumn(name = "picture_id")
  private Picture picture; // pictureId

  private LocalDate birthday;

  @CreatedDate
  private LocalDateTime createdAt;
  @LastModifiedDate
  private LocalDateTime modifiedAt;

  @Builder
  public User(Long id, String nickname,
      String email, String provider, String providerId,
      Gender gender,
      Picture picture, LocalDate birthday, LocalDateTime createdAt, LocalDateTime modifiedAt) {
    this.id = id;
    this.nickname = nickname;
    this.email = email;
    this.provider = provider;
    this.providerId = providerId;
    this.gender = gender;
    this.picture = picture;
    this.birthday = birthday;
    this.createdAt = createdAt;
    this.modifiedAt = modifiedAt;
  }

  boolean isAuthenticated() {
    return this.getGender() != null && this.getBirthday() != null && this.getNickname() != null;
  }

}