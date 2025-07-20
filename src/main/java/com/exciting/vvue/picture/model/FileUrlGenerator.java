package com.exciting.vvue.picture.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileUrlGenerator {

  public static String generate(String fileType, PictureUsedFor usedFor, Long id) {
    return fileType + "/" + usedFor.getName() + "/" + id + "/" + getUploadDate();
  }

  private static String getUploadDate() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
    return sdf.format(new Date());
  }
}
