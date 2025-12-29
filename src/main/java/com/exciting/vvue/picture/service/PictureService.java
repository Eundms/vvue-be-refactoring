package com.exciting.vvue.picture.service;

import com.exciting.vvue.picture.model.AccessLevel;
import com.exciting.vvue.picture.model.Picture;
import java.util.List;

public interface PictureService {

  List<Long> insertMulti(List<String> filePaths, AccessLevel accessLevel)
;

  Long insertSingle(String filePath, AccessLevel accessLevel);

  Picture getSingle(Long imageId);

  void deleteSingle(Long imageId);

  void deleteMulti(List<Long> imageIdList);
}
