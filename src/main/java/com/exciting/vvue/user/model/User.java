package com.exciting.vvue.user.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Fetch;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.exciting.vvue.picture.model.Picture;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
		@UniqueConstraint(name = "uk_provider_providerId", columnNames = {"provider", "provider_id"})
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

	@Column(name="provider_id")
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