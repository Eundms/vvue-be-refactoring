package com.exciting.vvue.married.service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarriedCodeServiceImpl implements MarriedCodeService{
	private final RedisTemplate<String, Long> redisEventTemplate;
	private final String variation = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	@Override
	public String generateCode(int length, int failoverCount) {
		String code = null;

		for(int times = 0; times < failoverCount; times++){
			String newCode = generate(length);
			if(!redisEventTemplate.hasKey(newCode)){
				code = newCode;
				break;
			}
		}
		return code;
	}

	@Override
	public boolean isCodeInRedis(String code) {
		return redisEventTemplate.hasKey(code);
	}

	@Override
	public void addMarriedCodeInRedis(Long id, String code) {
		redisEventTemplate.opsForValue().set(code, id, 5, TimeUnit.MINUTES);
	}

	@Override
	public Long getIdFromMarriedCode(String code) {
		return redisEventTemplate.opsForValue().get(code);
	}

	@Override
	public void deleteMarriedCodeInRedis(String code) {
		redisEventTemplate.delete(code);
	}

	private String generate(int length){
		Random random = new Random();
		StringBuffer code = new StringBuffer();
		for(int i = 0; i < length; i++)
			code.append(variation.charAt(random.nextInt(variation.length())));

		return code.toString();
	}
}
