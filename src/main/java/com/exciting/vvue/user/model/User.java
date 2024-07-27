package com.exciting.vvue.user.model;

import com.exciting.vvue.picture.model.Picture;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@ToString
@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickname;
    private String email;
    private String provider;
    @Column(unique = true)
    private String providerId;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = true)
    private Gender gender;
    private boolean isAuthenticated;
    @OneToOne(cascade = CascadeType.ALL)
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
                boolean isAuthenticated,
                Picture picture, LocalDate birthday, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
        this.gender = gender;
        this.isAuthenticated = isAuthenticated;
        this.picture = picture;
        this.birthday = birthday;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
