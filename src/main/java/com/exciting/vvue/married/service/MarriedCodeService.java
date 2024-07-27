package com.exciting.vvue.married.service;

public interface MarriedCodeService {
	// 코드 발급
	public String generateCode(int length, int failoverCount);

	// redis에서 확인
	public boolean isCodeInRedis(String code);

	// redis에 key-value 넣기
	public void addMarriedCodeInRedis(Long id, String code);


	// redis key값에 대한 value return
	public Long getIdFromMarriedCode(String code);

	// redis 안의 key 삭제
	public void deleteMarriedCodeInRedis(String code);
}
