package com.exciting.vvue.auth.repository;


import com.exciting.vvue.auth.model.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class AuthRepository {

    private static final long EXPIRATION_TIME_SECONDS =
        1000 * 60 * 60 * 10; // 10 hour - refresh token 시간이랑 동일하게
    private final RedisTemplate<String, Auth> redisTemplate;

    public void save(Auth auth) {
        String key = String.valueOf(auth.getUserId());
        redisTemplate.opsForValue().set(key, auth, EXPIRATION_TIME_SECONDS, TimeUnit.SECONDS);
    }

    public Auth findById(Long userId) {
        // Use userId as the Redis key
        String key = String.valueOf(userId);
        return redisTemplate.opsForValue().get(key);
    }
}
