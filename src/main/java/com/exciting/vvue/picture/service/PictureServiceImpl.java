package com.exciting.vvue.picture.service;

import com.exciting.vvue.common.exception.ErrorCode;
import com.exciting.vvue.common.exception.VvueException;
import com.exciting.vvue.picture.model.AccessLevel;
import com.exciting.vvue.picture.model.Picture;
import com.exciting.vvue.picture.repository.PictureRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PictureServiceImpl implements PictureService {

  private final PictureRepository pictureRepository;

  @Override
  public List<Long> insertMulti(List<String> filePaths, AccessLevel accessLevel)
       {
    List<Long> imagesIdList = new ArrayList<>();

    for (int i = 0; i < filePaths.size(); i++) {
      Picture newImage = Picture.builder()
          .url(filePaths.get(i))
          .accessLevel(accessLevel)
          .isDeleted(false)
          .build();
      Long id = pictureRepository.save(newImage).getId();
      imagesIdList.add(id);
    }

    return imagesIdList;
  }

  @Override
  public Long insertSingle(String filePath, AccessLevel accessLevel)
       {
    Picture newImage = Picture.builder()
        .url(filePath)
        .accessLevel(accessLevel)
        .isDeleted(false)
        .build();
    return pictureRepository.save(newImage).getId();
  }

  @Override
  public Picture getSingle(Long imageId) {
    return pictureRepository.findById(imageId).get();
  }

  @Override
  public void deleteSingle(Long pictureId)  {
    Picture deletedPicture = pictureRepository.findById(pictureId).get();
    pictureRepository.delete(deletedPicture);
  }

  @Override
  public void deleteMulti(List<Long> imageIdList)  {
    for (Long imageId : imageIdList) {
      this.deleteSingle(imageId);
    }
  }
}
