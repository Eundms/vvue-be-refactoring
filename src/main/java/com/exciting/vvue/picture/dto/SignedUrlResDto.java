package com.exciting.vvue.picture.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignedUrlResDto {

  private Long pictureId;
  private String signedUrl;

  public static SignedUrlResDto of(Long pictureId, String signedUrl) {
    return new SignedUrlResDto(pictureId, signedUrl);
  }
}
