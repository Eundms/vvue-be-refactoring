package com.exciting.vvue.picture.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.time.Instant;
import java.security.PrivateKey;
import java.security.Signature;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.List;

@Service
public class CloudFrontService {

	// CloudFront 관련 설정 값 주입
	@Value("${cloud.aws.cloudfront.key-pair-id}")
	private String cloudfrontKeyPairId;

	@Value("${cloud.aws.cloudfront.domain}")
	private String cloudfrontDomain;

	@Value("${cloud.aws.cloudfront.private-key-path}")
	private String privateKeyPath;

	/**
	 * 서명된 쿠키를 생성하는 메서드
	 */
	public List<String> generateSignedCookies() {
		try {
			String policy = createPolicy(); // 정책 생성
			String signature = signPolicy(policy); // 정책 서명
			return createSignedCookies(policy, signature); // 쿠키 생성
		} catch (Exception e) {
			throw new RuntimeException("Failed to generate signed cookie", e); // 예외 발생 시 메시지 제공
		}
	}

	/**
	 * CloudFront 접근 정책을 생성하는 메서드
	 * @return 생성된 정책 (JSON 형식)
	 */
	private String createPolicy() {
		long expirationTime = Instant.now().getEpochSecond() + 604800; // 7일 후 만료
		return "{"
			+ "\"Statement\":[{"
			+ "\"Resource\":\"" + cloudfrontDomain + "/*\","
			+ "\"Condition\":{"
			+ "\"DateLessThan\":{\"AWS:EpochTime\":" + expirationTime + "}"
			+ "}}]}";
	}

	/**
	 * 정책을 서명하는 메서드
	 * @param policy 서명할 정책
	 * @return 서명된 정책
	 */
	private String signPolicy(String policy) {
		try {
			// 개인 키 로드
			PrivateKey privateKey = loadPrivateKey(privateKeyPath);

			// 서명 생성
			Signature signature = Signature.getInstance("SHA256withRSA");
			signature.initSign(privateKey);
			signature.update(policy.getBytes(StandardCharsets.UTF_8));
			byte[] signedData = signature.sign();

			// 서명된 데이터를 Base64로 인코딩하여 반환
			return Base64.getEncoder().encodeToString(signedData);
		} catch (Exception e) {
			throw new RuntimeException("Error signing policy", e); // 서명 에러 시 처리
		}
	}

	/**
	 * 서명된 쿠키를 생성하는 메서드
	 * @param policy 서명된 정책
	 * @param signature 서명된 정책의 서명
	 * @return 서명된 쿠키
	 */
	private List<String> createSignedCookies(String policy, String signature) {
		List<String> cookies = new ArrayList<>();

		String cookiePolicy = "CloudFront-Policy=" + Base64.getEncoder().encodeToString(policy.getBytes(StandardCharsets.UTF_8));
		String cookieSignature = "CloudFront-Signature=" + signature;
		String cookieKeyPairId = "CloudFront-Key-Pair-Id=" + cloudfrontKeyPairId;

		// 각 쿠키 항목을 별도의 Set-Cookie 헤더로 반환
		cookies.add(cookiePolicy);
		cookies.add(cookieSignature);
		cookies.add(cookieKeyPairId);

		return cookies;
	}


	/**
	 * PEM 파일에서 개인 키를 로드하는 메서드
	 * @param privateKeyPath 개인 키 파일 경로
	 * @return 로드된 개인 키
	 */
	private PrivateKey loadPrivateKey(String privateKeyPath) {
		try {
			// PEM 파일을 읽어옵니다
			byte[] keyBytes = Files.readAllBytes(Paths.get(privateKeyPath));

			// PEM 형식에서 '-----BEGIN PRIVATE KEY-----' 및 '-----END PRIVATE KEY-----' 부분을 제거하고 디코딩합니다
			String key = new String(keyBytes);
			key = key.replace("-----BEGIN PRIVATE KEY-----", "")
				.replace("-----END PRIVATE KEY-----", "")
				.replaceAll("\\s", ""); // 공백 제거

			// Base64 디코딩하여 바이트 배열로 변환
			byte[] decodedKey = Base64.getDecoder().decode(key);

			// RSA 개인 키로 변환
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return keyFactory.generatePrivate(keySpec);  // RSAPrivateKey 객체 반환
		} catch (Exception e) {
			throw new RuntimeException("Failed to load private key", e); // 개인 키 로드 실패 시 처리
		}
	}
}
