package com.exciting.vvue.marriage.service;

public interface MarriedCodeRepository {
	String createCode(int length, int failOverCount);

	boolean isCodeExists(String code);

	void addMarriedCode(Long id, String code);

	Long getIdFromMarriedCode(String code);

	void deleteMarriedCode(String code);
}
