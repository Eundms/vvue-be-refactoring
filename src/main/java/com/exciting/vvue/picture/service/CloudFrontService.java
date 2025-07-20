package com.exciting.vvue.picture.service;

import java.io.FileReader;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cloudfront.CloudFrontUtilities;
import software.amazon.awssdk.services.cloudfront.cookie.CookiesForCustomPolicy;
import software.amazon.awssdk.services.cloudfront.model.CustomSignerRequest;

@Service
public class CloudFrontService {

  private static final Logger logger = LoggerFactory.getLogger(CloudFrontService.class);
  private static final CloudFrontUtilities cloudFrontUtilities = CloudFrontUtilities.create();

  @Value("${cloud.aws.cloudfront.key-pair-id}")
  private String cloudfrontKeyPairId;

  @Value("${cloud.aws.cloudfront.domain}")
  private String cloudfrontDomain;

  @Value("${cloud.aws.cloudfront.private-key-path}")
  private String privateKeyPath;

  @Value("${cookie.domain}")
  private String cookieDomain;

  /**
   * 서명된 쿠키를 생성하는 메서드
   */
  public List<String> generateSignedCookies() {
    try {
      // 개인 키 로드
      PrivateKey privateKey = loadPrivateKey(privateKeyPath);

      // CustomSignerRequest 객체 생성
      CustomSignerRequest customSignerRequest = CustomSignerRequest.builder()
          .resourceUrl(cloudfrontDomain + "/*")
          .keyPairId(cloudfrontKeyPairId)
          .privateKey(privateKey)
          .expirationDate(Instant.now().plusSeconds(604800))  // 7일 후 만료
          .activeDate(Instant.now())
          .build();

      // 서명된 쿠키 생성
      CookiesForCustomPolicy cookiesForCustomPolicy = cloudFrontUtilities.getCookiesForCustomPolicy(
          customSignerRequest);

      List<String> cookies = new ArrayList<>();
      String security = "; SameSite=None; Secure; Domain=" + cookieDomain + "; HttpOnly; Path=/";
      cookies.add(cookiesForCustomPolicy.policyHeaderValue() + security);
      cookies.add(cookiesForCustomPolicy.signatureHeaderValue() + security);
      cookies.add(cookiesForCustomPolicy.keyPairIdHeaderValue() + security);

      logger.info("Generated signed cookies successfully");
      return cookies;

    } catch (Exception e) {
      throw new RuntimeException("Failed to generate signed cookies", e);
    }
  }


  /**
   * PEM 형식의 개인 키 파일을 로드하는 메서드
   *
   * @param privateKeyPath 개인 키 파일 경로
   * @return 개인 키
   */
  private PrivateKey loadPrivateKey(String privateKeyPath) {
    try (PEMParser pemReader = new PEMParser(new FileReader(privateKeyPath))) {
      PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) pemReader.readObject();

      byte[] privateKeyBytes = privateKeyInfo.getEncoded();

      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
      return keyFactory.generatePrivate(keySpec);
    } catch (IOException | NoSuchAlgorithmException |
             java.security.spec.InvalidKeySpecException e) {
      throw new RuntimeException("Failed to load private key", e);
    }
  }
}
