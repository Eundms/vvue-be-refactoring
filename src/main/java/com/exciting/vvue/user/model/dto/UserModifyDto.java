package com.exciting.vvue.user.model.dto;

import com.exciting.vvue.user.model.Gender;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@Getter@Setter
public class UserModifyDto {
    private Long pictureId;
    private Gender gender;
    private String nickname;
    private LocalDate birthday;
}
