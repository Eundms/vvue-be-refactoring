package com.exciting.vvue.memory.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.exciting.vvue.picture.model.Picture;
import com.exciting.vvue.user.model.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "USERMEMORY")
public class UserMemory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name= "schedule_memory_id", nullable = false)
	private ScheduleMemory scheduleMemory;
	@OneToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	private String comment;

	@OneToOne(orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "picture_id", nullable = false)
	private Picture picture;

	@Builder
	public UserMemory(Long id, ScheduleMemory scheduleMemory, User user, String comment,
		Picture picture) {
		this.id = id;
		this.scheduleMemory = scheduleMemory;
		this.user = user;
		this.comment = comment;
		this.picture = picture;
	}

	public static UserMemory with(String comment, Picture picture, ScheduleMemory scheduleMemory,
		User user) {
		return UserMemory.builder()
			.scheduleMemory(scheduleMemory)
			.user(user)
			.comment(comment)
			.picture(picture)
			.build();
	}
}
