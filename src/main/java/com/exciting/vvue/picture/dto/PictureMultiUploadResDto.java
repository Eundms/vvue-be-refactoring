package com.exciting.vvue.picture.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PictureMultiUploadResDto {

  private MetaReqDto meta;
  private List<Long> pictureIdList;

  @Builder
  public PictureMultiUploadResDto(MetaReqDto meta, List<Long> pictureIdList) {
    this.meta = meta;
    this.pictureIdList = pictureIdList;
  }
}
