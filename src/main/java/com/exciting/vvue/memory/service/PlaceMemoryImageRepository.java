package com.exciting.vvue.memory.service;

import java.util.List;
import com.exciting.vvue.memory.model.PlaceMemoryImage;

public interface PlaceMemoryImageRepository {
	void save(PlaceMemoryImage build);

	void saveAll(List<PlaceMemoryImage> placeMemoryImages);
}

