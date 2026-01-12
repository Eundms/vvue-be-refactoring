package com.exciting.vvue.picture;

import com.exciting.vvue.auth.AuthContext;
import com.exciting.vvue.married.MarriedService;
import com.exciting.vvue.picture.dto.MetaReqDto;
import com.exciting.vvue.picture.dto.PictureIdList;
import com.exciting.vvue.picture.dto.PictureMultiUploadResDto;
import com.exciting.vvue.picture.dto.PictureSingleUploadResDto;
import com.exciting.vvue.common.exception.ErrorCode;
import com.exciting.vvue.common.exception.VvueException;
import com.exciting.vvue.picture.model.FileUrlGenerator;
import com.exciting.vvue.picture.model.Picture;
import com.exciting.vvue.picture.model.PictureUsedFor;
import com.exciting.vvue.picture.service.CloudFrontService;
import com.exciting.vvue.picture.service.PictureService;
import com.exciting.vvue.picture.util.FileManageUtil;
import java.util.ArrayList;
import java.util.List;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
public class PictureController implements PictureApi {

  private final PictureService pictureService;
  private final MarriedService marriedService;
  private final FileManageUtil fileManageUtil;
  private final CloudFrontService cloudFrontService;

  @Transactional
  @PostMapping("/upload/multi")
  @Override
  public PictureMultiUploadResDto uploadMulti(
      @RequestPart(value = "meta") MetaReqDto meta,
      @RequestPart(value = "pictures", required = false) List<MultipartFile> multipartFiles) {
    if (multipartFiles.isEmpty()) {
      throw new VvueException(ErrorCode.FILE_UPLOAD_FAILED);
    }

    Long userId = AuthContext.getUserId();
    Long id = getKeyOfDirectory(meta.getUsedFor(), userId);

    log.debug("[POST] /pictures/upload/multi : muiltipartfiles number " + multipartFiles.size());
    List<String> filePaths = new ArrayList<>();
    for (MultipartFile file : multipartFiles) {
      if (file != null && !file.isEmpty()) {
        String uploadFilePath = FileUrlGenerator.generate("image", meta.getUsedFor(), id);
        String filePath = fileManageUtil.uploadFile(file, uploadFilePath);
        filePaths.add(filePath);
      } else {
        throw new VvueException(ErrorCode.FILE_UPLOAD_FAILED);
      }
    }

    List<Long> imagesIdList = pictureService.insertMulti(filePaths,
        meta.getUsedFor().getAccessLevel());
    log.debug("[POST] /pictures/upload/multi : image IDs " + imagesIdList.toString());

    return PictureMultiUploadResDto.builder()
        .meta(meta)
        .pictureIdList(imagesIdList).build();
  }

  @Transactional
  @PostMapping("/upload/single")
  @Override
  public PictureSingleUploadResDto uploadSingle(
      @RequestPart(value = "meta") MetaReqDto meta,
      @RequestPart(value = "picture", required = false) MultipartFile multipartFile) {
    Long userId = AuthContext.getUserId();

    if (multipartFile == null || multipartFile.isEmpty()) {
      throw new VvueException(ErrorCode.FILE_UPLOAD_FAILED);
    }

    Long id = getKeyOfDirectory(meta.getUsedFor(), userId);

    String uploadFilePath = FileUrlGenerator.generate("image", meta.getUsedFor(), id);

    String filePath = fileManageUtil.uploadFile(multipartFile, uploadFilePath);

    Long pictureId = pictureService.insertSingle(filePath, meta.getUsedFor().getAccessLevel());

    return PictureSingleUploadResDto.builder()
        .meta(meta)
        .id(pictureId)
        .build();
  }

  @Transactional
  @DeleteMapping("/single/{pictureId}")
  @Override
  public void delete(@PathVariable("pictureId") Long id) {
    if (pictureService.getSingle(id) == null) {
      throw new VvueException(ErrorCode.FILE_DELETE_FAILED);
    }
    pictureService.deleteSingle(id);
  }

  @Transactional
  @DeleteMapping("/multi")
  @Override
  public void deleteMulti(@RequestBody PictureIdList pictureIdList) {
    if (pictureIdList.getPictureIds() == null || pictureIdList.getPictureIds().isEmpty()) {
      throw new VvueException(ErrorCode.FILE_DELETE_FAILED);
    }
    pictureService.deleteMulti(pictureIdList.getPictureIds());
  }

  @GetMapping("/signed/{pictureId}")
  @Override
  public String getSignedPictureUrl(@PathVariable("pictureId") Long pictureId) {
    Picture picture = pictureService.getSingle(pictureId);
    if (picture == null) {
      throw new VvueException(ErrorCode.PICTURE_NOT_FOUND);
    }

    String s3Key = extractS3KeyFromUrl(picture.getUrl());

    return cloudFrontService.generateSignedUrl(s3Key);
  }

  private String extractS3KeyFromUrl(String url) {
    // If URL is like "https://domain.com/images/user/123/image.jpg"
    // Extract just "images/user/123/image.jpg"
    if (url.contains("/images/")) {
      return url.substring(url.indexOf("/images/") + 1);
    }
    return url; // fallback
  }

  private Long getKeyOfDirectory(PictureUsedFor usedFor, Long userId) {
    Long id = userId;
    if (usedFor == PictureUsedFor.MARRIED_RELATED) {
      id = marriedService.getMarriedIdByUserId(userId);
    }
    return id;
  }
}
