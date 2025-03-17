package com.exciting.vvue.married.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.Version;

import com.exciting.vvue.picture.model.Picture;
import com.exciting.vvue.user.model.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@Table(name = "MARRIED", uniqueConstraints = {
	@UniqueConstraint(name = "unique_first_second", columnNames = {"first_id", "second_id"})
})
public class Married {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDate marriedDay;

	@OneToOne(orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "picture_id")
	private Picture picture; // pictureId

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private User first;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private User second;

	@Builder
	public Married(Long id, LocalDate marriedDay, Picture picture, User first, User second) {
		this.id = id;
		this.marriedDay = marriedDay;
		this.picture = picture;
		this.first = first;
		this.second = second;
	}

	public boolean isMarried(Long userId) { // 두명이 부부인지 확인
		return first != null && first.getId() == userId
			|| second != null && second.getId() == userId;
	}

	public boolean isAllInfoUpdated() {
		return !(this.getFirst() == null || this.getSecond() == null
			|| this.getMarriedDay() == null);
	}

	public Long getPeerId(Long userId) {
		if(this.getSecond().getId() == userId){
			return this.getFirst().getId();
		}
		return this.getSecond().getId();
	}
}
