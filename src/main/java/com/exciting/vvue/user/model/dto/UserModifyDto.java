package com.exciting.vvue.user.model.dto;

import java.time.LocalDate;

import com.exciting.vvue.user.model.Gender;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class UserModifyDto {
	private Long pictureId;
	private Gender gender;
	private String nickname;
	private LocalDate birthday;
}
