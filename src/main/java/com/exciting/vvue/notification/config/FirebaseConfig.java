// package com.exciting.vvue.notification.config;
//
// import com.google.auth.oauth2.GoogleCredentials;
// import com.google.firebase.FirebaseApp;
// import com.google.firebase.FirebaseOptions;
// import com.google.firebase.messaging.FirebaseMessaging;
//
// import java.io.FileInputStream;
// import java.io.FileNotFoundException;
// import java.io.IOException;
// import java.io.InputStream;
// import lombok.RequiredArgsConstructor;
// import org.springframework.boot.context.properties.EnableConfigurationProperties;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
//
// @Configuration
// @EnableConfigurationProperties(FirebaseProperties.class)
// @RequiredArgsConstructor
// public class FirebaseConfig {
//
//     private final FirebaseProperties firebaseProperties;
//
//     @Bean
//     GoogleCredentials googleCredentials() {
//         String filePath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
//
//         // 파일을 읽어오기 위해 FileInputStream 사용
//         try (InputStream serviceAccount = new FileInputStream(filePath)) {
//             return GoogleCredentials.fromStream(serviceAccount);
//         } catch (IOException e) {
//             throw new RuntimeException(e);
//         }
//     }
//
//     @Bean
//     FirebaseApp firebaseApp(GoogleCredentials credentials) {
//         FirebaseOptions options = FirebaseOptions.builder()
//             .setCredentials(credentials)
//             .build();
//
//         return FirebaseApp.initializeApp(options);
//     }
//
//     @Bean
//     FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
//         return FirebaseMessaging.getInstance(firebaseApp);
//     }
// }
