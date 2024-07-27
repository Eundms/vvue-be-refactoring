package com.exciting.vvue.married.service;

import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.married.model.dto.MarriedModifyDto;
import com.exciting.vvue.married.model.dto.req.MarriedCreateDto;

public interface MarriedService {

    int getMarriedCount(Long id);

    // married 찾기
    Married getMarriedByUserId(Long id);

    // peerId 찾기

    Long getSpouseIdById(Long userId);

    // married 정보 수정하기
    void updateMarried(Long id, MarriedModifyDto marriedModifyDto);

    //married id 값으로 객체 있는지 확인
    boolean existById(Long id);

    void createMarried(Long id, MarriedCreateDto marriedCreateDto);

    boolean existByUserId(Long id);
    int countByUserId(Long id);

    Married deleteByUserId(Long id);
}
