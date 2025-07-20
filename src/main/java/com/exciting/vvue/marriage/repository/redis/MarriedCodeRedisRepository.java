package com.exciting.vvue.marriage.repository.redis;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MarriedCodeRedisRepository {

  private final RedisTemplate<String, Long> redisEventTemplate;
  private final String variation = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";


  public String createCode(int length, int failOverCount) {
    String code = null;

    for (int times = 0; times < failOverCount; times++) {
      String newCode = generate(length);
      if (!redisEventTemplate.hasKey(newCode)) {
        code = newCode;
        break;
      }
    }
    return code;
  }

  private String generate(int length) {
    Random random = new Random();
    StringBuffer code = new StringBuffer();
    for (int i = 0; i < length; i++) {
      code.append(variation.charAt(random.nextInt(variation.length())));
    }

    return code.toString();
  }


  public boolean isCodeInRedis(String code) {
    return redisEventTemplate.hasKey(code);
  }


  public void addMarriedCodeInRedis(Long id, String code) {
    redisEventTemplate.opsForValue().set(code, id, 5, TimeUnit.MINUTES);
  }


  public Long getIdFromMarriedCode(String code) {
    return redisEventTemplate.opsForValue().get(code);
  }


  public void deleteMarriedCodeInRedis(String code) {
    redisEventTemplate.delete(code);
  }

}
