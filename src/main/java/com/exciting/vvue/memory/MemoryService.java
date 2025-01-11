package com.exciting.vvue.memory;

import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.common.exception.memory.MemoryNotFoundException;
import com.exciting.vvue.memory.dto.req.MemoryAddReqDto;
import com.exciting.vvue.memory.dto.res.MemoryAlbumResDto;
import com.exciting.vvue.memory.dto.res.MemoryResDto;
import com.exciting.vvue.user.model.User;

public interface MemoryService {

	Long add(MemoryAddReqDto memoryAddReqDto, User user, Married userMarried);

	MemoryResDto getById(Long memoryId, User user) throws MemoryNotFoundException;

	void deleteById(Long memoryId, User user);

	MemoryAlbumResDto getAllThumbnail(Married married, Long nextCursor, int size);
}
