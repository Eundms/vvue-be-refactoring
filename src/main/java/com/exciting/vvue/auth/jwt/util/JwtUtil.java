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
			.setSubject(subject) // access-token , refresh-token
			.setIssuedAt(now)
			.setExpiration(expiryDate)
			.setIssuer(ISSUER)
			.signWith(key) // secret key를 이용한 암호화
			.compact(); // 직렬화 처리
	}

	public Map<String, Object> getClaims(String token) {
		Jws<Claims> claims = extractAllClaims(token);
		return claims.getBody();
	}

	public String getClaimBy(String token, String key) {
		Map<String, Object> allClaims = getClaims(token);
		return String.valueOf(allClaims.get(key));
	}

	public String extractSubject(String token) {
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

		// 발행자(issuer) 검증
		if (!isValidIssuer(claims.getBody())) {
			throw new JwtException("JWT Issuer Is Not Valid");
		}

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

	// isSignatureValid 메소드를 JJWT의 내장 메소드로 대체
	private boolean isSignatureValid(String jwt) {
		try {
			Jws<Claims> claims = extractAllClaims(jwt);  // 서명을 포함한 클레임을 추출
			// 서명이 유효한지 JJWT에서 자체적으로 처리해줌
			return true;
		} catch (JwtException e) {
			// 서명 검증 실패 시 false 반환
			return false;
		}
	}

	private boolean isValidIssuer(Claims claims) {
		// 발행자 검증 (예: "issuer" 클레임에 예상되는 발행자 정보가 들어있는지 확인)
		String issuer = claims.getIssuer();
		return ISSUER.equals(issuer);
	}
}
