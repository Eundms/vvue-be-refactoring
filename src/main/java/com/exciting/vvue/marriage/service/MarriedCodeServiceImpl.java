package com.exciting.vvue.marriage.service;

import org.springframework.stereotype.Service;

import com.exciting.vvue.marriage.MarriedCodeService;

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
	public boolean isCodeExists(String code) {
		return marriedCodeRepository.isCodeExists(code);
	}

	@Override
	public void addMarriedCode(Long id, String code) {
		marriedCodeRepository.addMarriedCode(id, code);
	}

	@Override
	public Long getIdFromMarriedCode(String code) {
		return marriedCodeRepository.getIdFromMarriedCode(code);
	}

	@Override
	public void deleteMarriedCode(String code) {
		marriedCodeRepository.deleteMarriedCode(code);
	}

}
