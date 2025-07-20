package com.exciting.vvue.memory.dto.res;

import com.exciting.vvue.picture.dto.PictureDto;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemoryAtPlaceDto {

  private Long memoryId;
  private String scheduleName;
  private LocalDate scheduleDate;
  private List<PictureDto> placePictures;
}
