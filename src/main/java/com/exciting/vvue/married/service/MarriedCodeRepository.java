package com.exciting.vvue.married.service;

public interface MarriedCodeRepository {
	String createCode(int length, int failOverCount);

	boolean isCodeInRedis(String code);

	void addMarriedCodeInRedis(Long id, String code);

	Long getIdFromMarriedCode(String code);

	void deleteMarriedCodeInRedis(String code);
}
