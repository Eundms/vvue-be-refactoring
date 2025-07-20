package com.exciting.vvue.picture;

import com.exciting.vvue.auth.AuthContext;
import com.exciting.vvue.married.MarriedService;
import com.exciting.vvue.picture.dto.MetaReqDto;
import com.exciting.vvue.picture.dto.PictureIdList;
import com.exciting.vvue.picture.dto.PictureMultiUploadResDto;
import com.exciting.vvue.picture.dto.PictureSingleUploadResDto;
import com.exciting.vvue.picture.exception.FileDeleteFailException;
import com.exciting.vvue.picture.exception.FileUploadFailException;
import com.exciting.vvue.picture.model.FileUrlGenerator;
import com.exciting.vvue.picture.model.PictureUsedFor;
import com.exciting.vvue.picture.service.PictureService;
import com.exciting.vvue.picture.util.FileManageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/pictures")
@RequiredArgsConstructor
public class PictureController {

  private final PictureService pictureService;
  private final MarriedService marriedService;
  private final FileManageUtil fileManageUtil;

  // 사진 여러 장 업로드
  @Transactional
  @PostMapping("/upload/multi")
  @Operation(description = "사진 여러장 업로드", summary = "장소 추억 전용")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "업로드 성공", content = {
          @Content(schema = @Schema(implementation = PictureMultiUploadResDto.class))}),
      @ApiResponse(responseCode = "400", description = "업로드 실패"),
      @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")
  })
  public ResponseEntity<?> uploadMulti(
      @RequestPart(value = "meta") MetaReqDto meta,
      @RequestPart(value = "pictures", required = false) List<MultipartFile> multipartFiles) {
    if (multipartFiles.isEmpty()) {
      throw new FileUploadFailException("업로드 할 이미지가 없어요");
    }

    Long userId = AuthContext.getUserId();
    Long id = getKeyOfDirectory(meta.getUsedFor(), userId);

    log.debug("[POST] /pictures/upload/multi : muiltipartfiles number " + multipartFiles.size());
    List<String> filePaths = new ArrayList<>();
    for (MultipartFile file : multipartFiles) {
      if (file != null && !file.isEmpty()) {
        String uploadFilePath = FileUrlGenerator.generate("image", meta.getUsedFor(), id);
        String filePath = fileManageUtil.uploadFile(file, uploadFilePath); // meta 포함하여 업로드
        filePaths.add(filePath);
      } else {
        throw new FileUploadFailException("이미지 업로드 중 문제가 발생하였습니다.");
      }
    }

    List<Long> imagesIdList = pictureService.insertMulti(filePaths,
        meta.getUsedFor().getAccessLevel());
    log.debug("[POST] /pictures/upload/multi : image IDs " + imagesIdList.toString());

    PictureMultiUploadResDto pictureMultiUploadResDto = PictureMultiUploadResDto.builder()
        .meta(meta)
        .pictureIdList(imagesIdList).build();
    return new ResponseEntity<>(pictureMultiUploadResDto, HttpStatus.OK);
  }

  // 사진 1장 업로드
  @Transactional
  @PostMapping("/upload/single")
  @Operation(description = "사진 1장 업로드", summary = "프로필 변경, 부부공유 사진 변경")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "업로드 성공", content = {
          @Content(schema = @Schema(implementation = PictureSingleUploadResDto.class))}),
      @ApiResponse(responseCode = "400", description = "업로드 실패"),
      @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")
  })
  public ResponseEntity<?> uploadSingle(
      @RequestPart(value = "meta") MetaReqDto meta,
      @RequestPart(value = "picture", required = false) MultipartFile multipartFile) {
    Long userId = AuthContext.getUserId();

    if (multipartFile == null || multipartFile.isEmpty()) {
      throw new FileUploadFailException("이미지 업로드 중 문제가 발생하였습니다.");
    }

    // 사진 업로드
    Long id = getKeyOfDirectory(meta.getUsedFor(), userId);

    String uploadFilePath = FileUrlGenerator.generate("image", meta.getUsedFor(), id);

    String filePath = fileManageUtil.uploadFile(multipartFile, uploadFilePath);

    Long pictureId = pictureService.insertSingle(filePath, meta.getUsedFor().getAccessLevel());

    PictureSingleUploadResDto pictureSingleUploadResDto = PictureSingleUploadResDto.builder()
        .meta(meta)
        .id(pictureId)
        .build();
    return new ResponseEntity<>(pictureSingleUploadResDto, HttpStatus.OK);
  }

  // 사진 1장 삭제
  @Transactional
  @DeleteMapping("/single/{pictureId}")
  @Operation(description = "사진 1장 삭제", summary = "사진 삭제")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "삭제 성공"),
      @ApiResponse(responseCode = "400", description = "삭제 실패"),
      @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")
  })
  public ResponseEntity<?> delete(@PathVariable("pictureId") Long id) {
    if (pictureService.getSingle(id) == null) {
      throw new FileDeleteFailException("없거나 이미 삭제한 이미지입니다");
    }
    pictureService.deleteSingle(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  // 여러 장 사진 삭제
  @Transactional
  @DeleteMapping("/multi")
  @Operation(description = "사진 여러장 삭제", summary = "사진 삭제")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "삭제 성공"),
      @ApiResponse(responseCode = "404", description = "삭제 실패"),
      @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")
  })
  public ResponseEntity<?> deleteMulti(@RequestBody PictureIdList pictureIdList) {

    if (pictureIdList.getPictureIds() == null || pictureIdList.getPictureIds().isEmpty()) {
      throw new FileDeleteFailException("삭제할 파일이 없어요.");
    }
    pictureService.deleteMulti(pictureIdList.getPictureIds());
    return new ResponseEntity<>(HttpStatus.OK);
  }

  private Long getKeyOfDirectory(PictureUsedFor usedFor, Long userId) {
    Long id = userId;
    if (usedFor == PictureUsedFor.MARRIED_RELATED) {
      id = marriedService.getMarriedIdByUserId(userId);
    }
    return id;
  }
}
