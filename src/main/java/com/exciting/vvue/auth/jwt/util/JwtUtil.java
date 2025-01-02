package com.exciting.vvue.auth.jwt.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {
	private final String ISSUER = "vvue-auth";
	private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

	public String generateToken(String subject, Map<String, Object> claims, long expiration) {
		return "Bearer " + createToken(claims, subject, expiration);
	}

	private String createToken(Map<String, Object> claims, String subject, long expiration) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + expiration);
		return Jwts.builder()
			.setClaims(claims)
			.setSubject(subject)// access-token , refresh-token
			.setIssuedAt(now)
			.setExpiration(expiryDate)
			.setIssuer(ISSUER)
			.signWith(key) // secret key를 이용한 암호화
			.compact(); //직렬화 처리
	}

	// {userId : }
	public Map<String, Object> getClaims(String token) {
		Jws<Claims> claims = extractAllClaims(token);
		return claims.getBody();
	}

	public String getClaimBy(String token, String key) {
		Map<String, Object> allClaims = getClaims(token);
		return String.valueOf(allClaims.get(key));
	}

	public String extractSubject(String token) { // access-token , refresh-token
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public Boolean validateToken(String jwt) throws JwtException {
		// 토큰의 클레임을 추출
		Jws<Claims> claims = extractAllClaims(jwt);
		log.debug("claims: {}", claims);

		// 만료 여부 체크
		if (isTokenExpired(jwt)) {
			throw new JwtException("JWT Is Expired");
		}

		// 서명 유효성 검사
		if (!isSignatureValid(jwt)) {
			throw new JwtException("JWT Signature Is Not Valid");
		}

		// 발행자(issuer) 및 대상(audience) 검증
		if (!isValidIssuer(claims.getBody())) {
			throw new JwtException("JWT Issuer Is Not Valid");
		}

		// if (!isValidAudience(claims.getBody())) {
		//     throw new JwtException("JWT 토큰의 대상이 유효하지 않습니다.");
		// }

		// 여기에 추가적인 검증 로직을 삽입할 수 있음
		return true;
	}

	private String removeBearer(String bearerToken) {
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.replace("Bearer ", "");
		}
		return bearerToken;
	}

	private Jws<Claims> extractAllClaims(String token) {
		token = removeBearer(token);
		return Jwts.parserBuilder().setSigningKey(key).build()
			.parseClaimsJws(token);
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Jws<Claims> claims = extractAllClaims(token);
		return claimsResolver.apply(claims.getBody());
	}

	private boolean isTokenExpired(String jwt) {
		Claims claims = extractAllClaims(jwt).getBody();
		return claims.getExpiration().before(new Date());
	}

	private boolean isSignatureValid(String jwt) {
		try {
			// JWT를 '.' 기준으로 분리
			String[] parts = jwt.split("\\.");
			if (parts.length != 3) {
				return false;  // 유효한 JWT 형식이 아님
			}

			// JWT에서 헤더와 페이로드 추출
			String header = parts[0];
			String payload = parts[1];
			String signature = parts[2];

			// HMAC 서명을 검증하기 위해 원본 데이터를 사용하여 서명을 생성
			String data = header + "." + payload;  // 헤더와 페이로드 결합
			String expectedSignature = createSignature(data);  // 서명 생성

			// 생성한 서명과 JWT의 서명이 일치하는지 확인
			return signature.equals(expectedSignature);
		} catch (Exception e) {
			// 서명 검증 중 오류가 발생하면 false 반환
			return false;
		}
	}

	// 서명 생성 (HMAC)
	private String createSignature(String data) {
		try {
			Mac hmac = Mac.getInstance("HmacSHA256");
			SecretKeySpec secretKey = new SecretKeySpec(key.getEncoded(), "HmacSHA256");
			hmac.init(secretKey);
			byte[] signatureBytes = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
			return Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);  // URL-safe base64 인코딩
		} catch (Exception e) {
			throw new RuntimeException("Error creating signature", e);
		}
	}

	private boolean isValidIssuer(Claims claims) {
		// 발행자 검증 (예: "issuer" 클레임에 예상되는 발행자 정보가 들어있는지 확인)
		String issuer = claims.getIssuer();
		return ISSUER.equals(issuer);
	}

	// private boolean isValidAudience(Claims claims) {
	//     // 대상 검증 (예: "audience" 클레임에 예상되는 대상 정보가 들어있는지 확인)
	//     String audience = claims.getAudience();
	//     return "your-expected-audience".equals(audience);
	// }

}