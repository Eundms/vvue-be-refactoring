package com.exciting.vvue.notification.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Subscriber {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Long id;

	@Column(unique = true)
	private Long userId;

	private String firebaseToken;

	@Builder
	public Subscriber(Long id, Long userId, String firebaseToken) {
		this.id = id;
		this.userId = userId;
		this.firebaseToken = firebaseToken;
	}

	public static Subscriber from(Long userId, String firebaseToken) {
		return Subscriber.builder()
			.userId(userId)
			.firebaseToken(firebaseToken)
			.build();
	}
}
