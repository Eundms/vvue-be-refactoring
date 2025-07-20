package com.exciting.vvue.married;

import com.exciting.vvue.married.dto.MarriedModifyDto;
import com.exciting.vvue.married.dto.req.MarriedCreateDto;
import com.exciting.vvue.married.model.Married;

public interface MarriedService {

  // married 찾기
  Married getMarriedByUserIdWithDetails(Long id);

  Long getMarriedIdByUserId(Long userId);

  // married 정보 수정하기
  Long updateMarriedAndReturnId(Long userId, MarriedModifyDto marriedModifyDto);

  Long createMarried(Long id, MarriedCreateDto marriedCreateDto);

  int countByUserId(Long id);

  void deleteByUserId(Long id);

  Long getSpouseId(Long userId);
}
