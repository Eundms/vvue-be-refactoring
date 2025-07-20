package com.exciting.vvue.marriage;

public interface MarriedCodeService {

  // 코드 발급
  String generateCode(int length, int failoverCount);

  // redis에서 확인
  boolean isCodeExists(String code);

  // redis에 key-value 넣기
  void addMarriedCode(Long id, String code);

  // redis key값에 대한 value return
  Long getIdFromMarriedCode(String code);

  // redis 안의 key 삭제
  void deleteMarriedCode(String code);
}
