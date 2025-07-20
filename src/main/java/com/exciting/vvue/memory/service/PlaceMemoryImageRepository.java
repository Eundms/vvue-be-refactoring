package com.exciting.vvue.memory.service;

import com.exciting.vvue.memory.model.PlaceMemoryImage;
import java.util.List;

public interface PlaceMemoryImageRepository {

  void save(PlaceMemoryImage build);

  void saveAll(List<PlaceMemoryImage> placeMemoryImages);
}

