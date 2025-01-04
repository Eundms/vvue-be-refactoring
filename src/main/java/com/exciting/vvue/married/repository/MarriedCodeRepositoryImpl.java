package com.exciting.vvue.married.repository;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.exciting.vvue.married.repository.jpa.MarriedCodeRedisRepository;
import com.exciting.vvue.married.service.MarriedCodeRepository;

import lombok.RequiredArgsConstructor;

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
