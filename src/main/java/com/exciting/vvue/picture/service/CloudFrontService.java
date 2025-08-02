package com.exciting.vvue.picture.service;

import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cloudfront.CloudFrontUtilities;
import software.amazon.awssdk.services.cloudfront.cookie.CookiesForCustomPolicy;
import software.amazon.awssdk.services.cloudfront.model.CannedSignerRequest;
import software.amazon.awssdk.services.cloudfront.model.CustomSignerRequest;
import software.amazon.awssdk.services.cloudfront.url.SignedUrl;

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
      CloudFrontUtilities utilities = CloudFrontUtilities.create();

      // CustomSignerRequest 객체 생성
      CustomSignerRequest customSignerRequest = CustomSignerRequest.builder()
          .resourceUrl(cloudfrontDomain + "/*")
          .keyPairId(cloudfrontKeyPairId)
          .privateKey(Paths.get(privateKeyPath))
          .expirationDate(Instant.now().plus(Duration.ofDays(7)))
          .activeDate(Instant.now())
          .build();

      // 서명된 쿠키 생성
      CookiesForCustomPolicy cookiesForCustomPolicy = utilities.getCookiesForCustomPolicy(
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
   * 개별 사진에 대한 서명된 URL 생성
   */
  public String generateSignedUrl(String s3Key) {
    try {
      CloudFrontUtilities utilities = CloudFrontUtilities.create();
      
      CannedSignerRequest request = CannedSignerRequest.builder()
          .resourceUrl(cloudfrontDomain + "/" + s3Key)
          .privateKey(Paths.get(privateKeyPath))
          .keyPairId(cloudfrontKeyPairId)
          .expirationDate(Instant.now().plus(Duration.ofHours(1)))
          .build();
          
      SignedUrl signedUrl = utilities.getSignedUrlWithCannedPolicy(request);
      logger.info("Generated signed URL for key: {}", s3Key);
      return signedUrl.url();
      
    } catch (Exception e) {
      throw new RuntimeException("Failed to generate signed URL for key: " + s3Key, e);
    }
  }


}
