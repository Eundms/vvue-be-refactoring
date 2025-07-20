package com.exciting.vvue.marriage.repository;

import com.exciting.vvue.marriage.repository.redis.MarriedCodeRedisRepository;
import com.exciting.vvue.marriage.service.MarriedCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MarriedCodeRepositoryImpl implements MarriedCodeRepository {

  private final MarriedCodeRedisRepository marriedCodeRedisRepository;

  @Override
  public String createCode(int length, int failOverCount) {
    return marriedCodeRedisRepository.createCode(length, failOverCount);
  }

  @Override
  public boolean isCodeExists(String code) {
    return marriedCodeRedisRepository.isCodeInRedis(code);
  }

  @Override
  public void addMarriedCode(Long id, String code) {
    marriedCodeRedisRepository.addMarriedCodeInRedis(id, code);
  }

  @Override
  public Long getIdFromMarriedCode(String code) {
    return marriedCodeRedisRepository.getIdFromMarriedCode(code);
  }

  @Override
  public void deleteMarriedCode(String code) {
    marriedCodeRedisRepository.deleteMarriedCodeInRedis(code);
  }

}
