package com.exciting.vvue.notification.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableConfigurationProperties(FirebaseProperties.class)
@RequiredArgsConstructor
@Profile("!test")
public class FirebaseConfig {

  private final FirebaseProperties firebaseProperties;
  @Value("${cloud.gcp.firebase.service-account-path}")
  private String filePath;

  @Bean
  GoogleCredentials googleCredentials() {
    // 파일 존재 여부 확인
    if (!Files.exists(Paths.get(filePath))) {
      throw new RuntimeException("Firebase service account file not found: " + filePath);
    }

    // 파일을 읽어오기 위해 FileInputStream 사용
    try (InputStream serviceAccount = new FileInputStream(filePath)) {
      return GoogleCredentials.fromStream(serviceAccount);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Bean
  FirebaseApp firebaseApp(GoogleCredentials credentials) {
    FirebaseOptions options = FirebaseOptions.builder()
        .setCredentials(credentials)
        .build();

    return FirebaseApp.initializeApp(options);
  }

  @Bean
  FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
    return FirebaseMessaging.getInstance(firebaseApp);
  }
}
