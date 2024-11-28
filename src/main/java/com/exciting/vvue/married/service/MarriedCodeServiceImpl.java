package com.exciting.vvue.married.service;

import org.springframework.stereotype.Service;

import com.exciting.vvue.married.MarriedCodeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarriedCodeServiceImpl implements MarriedCodeService {
	private final MarriedCodeRepository marriedCodeRepository;

	@Override
	public String generateCode(int length, int failoverCount) {
		return marriedCodeRepository.createCode(length, failoverCount);
	}

	@Override
	public boolean isCodeInRedis(String code) {
		return marriedCodeRepository.isCodeInRedis(code);
	}

	@Override
	public void addMarriedCodeInRedis(Long id, String code) {
		marriedCodeRepository.addMarriedCodeInRedis(id, code);
	}

	@Override
	public Long getIdFromMarriedCode(String code) {
		return marriedCodeRepository.getIdFromMarriedCode(code);
	}

	@Override
	public void deleteMarriedCodeInRedis(String code) {
		marriedCodeRepository.deleteMarriedCodeInRedis(code);
	}


}
