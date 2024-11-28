package com.exciting.vvue.auth.service;


import com.exciting.vvue.auth.model.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

public interface AuthRepository {

    void save(Auth auth);

    Auth findById(Long userId);
}
