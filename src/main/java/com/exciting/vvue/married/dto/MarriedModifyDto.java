package com.exciting.vvue.married.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "부부 정보 수정", description = "부부 정보 수정을 위함")
public class MarriedModifyDto {

  @SchemaProperty(name = "결혼기념일")
  private LocalDate marriedDay;
  @SchemaProperty(name = "사진 id, url")
  private Long pictureId;

}
