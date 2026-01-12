package com.exciting.vvue.picture;

import com.exciting.vvue.picture.dto.MetaReqDto;
import com.exciting.vvue.picture.dto.PictureIdList;
import com.exciting.vvue.picture.dto.PictureMultiUploadResDto;
import com.exciting.vvue.picture.dto.PictureSingleUploadResDto;
import com.exciting.vvue.picture.dto.SignedUrlResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

public interface PictureApi {

  @Operation(description = "사진 여러장 업로드", summary = "장소 추억 전용")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "업로드 성공", content = {
          @Content(schema = @Schema(implementation = PictureMultiUploadResDto.class))}),
      @ApiResponse(responseCode = "400", description = "업로드 실패"),
      @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")
  })
  PictureMultiUploadResDto uploadMulti(
      @RequestPart(value = "meta") MetaReqDto meta,
      @RequestPart(value = "pictures", required = false) List<MultipartFile> multipartFiles);

  @Operation(description = "사진 1장 업로드", summary = "프로필 변경, 부부공유 사진 변경")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "업로드 성공", content = {
          @Content(schema = @Schema(implementation = PictureSingleUploadResDto.class))}),
      @ApiResponse(responseCode = "400", description = "업로드 실패"),
      @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")
  })
  PictureSingleUploadResDto uploadSingle(
      @RequestPart(value = "meta") MetaReqDto meta,
      @RequestPart(value = "picture", required = false) MultipartFile multipartFile);

  @Operation(description = "사진 1장 삭제", summary = "사진 삭제")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "삭제 성공"),
      @ApiResponse(responseCode = "400", description = "삭제 실패"),
      @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")
  })
  void delete(@PathVariable("pictureId") Long id);

  @Operation(description = "사진 여러장 삭제", summary = "사진 삭제")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "삭제 성공"),
      @ApiResponse(responseCode = "404", description = "삭제 실패")
  })
  void deleteMulti(@RequestBody PictureIdList pictureIdList);

  @Operation(description = "Get signed URL for picture access", summary = "Secure picture access")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Signed URL generated successfully"),
      @ApiResponse(responseCode = "404", description = "Picture not found")
  })
  SignedUrlResDto getSignedPictureUrl(@PathVariable("pictureId") Long pictureId);
}
