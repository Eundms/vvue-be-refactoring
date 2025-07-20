package com.exciting.vvue.picture.service;

import com.exciting.vvue.picture.exception.FileDeleteFailException;
import com.exciting.vvue.picture.exception.FileUploadFailException;
import com.exciting.vvue.picture.model.AccessLevel;
import com.exciting.vvue.picture.model.Picture;
import java.util.List;

public interface PictureService {

  List<Long> insertMulti(List<String> filePaths, AccessLevel accessLevel)
      throws FileUploadFailException;

  Long insertSingle(String filePath, AccessLevel accessLevel) throws FileUploadFailException;

  Picture getSingle(Long imageId);

  void deleteSingle(Long imageId) throws FileDeleteFailException;

  void deleteMulti(List<Long> imageIdList) throws FileDeleteFailException;
}
