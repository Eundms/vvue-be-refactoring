package com.exciting.vvue.user.dto;

import com.exciting.vvue.user.model.Gender;
import java.time.LocalDate;
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
