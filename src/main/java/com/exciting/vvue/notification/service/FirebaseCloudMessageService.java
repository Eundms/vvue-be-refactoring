//package com.exciting.vvue.notification.service;
//
//import com.fasterxml.jackson.core.JsonParseException;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.common.net.HttpHeaders;
//import com.google.firebase.messaging.Message;
//import com.google.firebase.messaging.Notification;
//import java.io.IOException;
//import java.util.List;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import okhttp3.MediaType;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class FirebaseCloudMessageService {
//
//    private final String API_URL = "https://fcm.googleapis.com/v1/projects/vvue-login/messages:send";
//    private final ObjectMapper objectMapper;
//
//    public void sendMessageTo(String targetToken, String title, String body){
//        String message =null;
//        OkHttpClient client=null;
//        Request request=null;
//        try {
//            message = makeMessage(targetToken, title, body);
//         client = new OkHttpClient();
//        RequestBody requestBody = RequestBody.create(message,
//            MediaType.get("application/json; charset=utf-8"));
//        request = new Request.Builder()
//            .url(API_URL)
//            .post(requestBody)
//            .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
//            .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
//            .build();
//            Response response = client.newCall(request).execute();
//            System.out.println(response.body().string());
//
//        }catch (IOException e){
//            log.debug(e.getMessage());
//        }
//
//    }
//
//    private String makeMessage(String targetToken, String title, String body)
//        throws JsonProcessingException {
//        Message message = Message.builder()
//            .putData("score","850")
//            .putData("time","2:45")
//            .setToken(targetToken)
//            .setNotification(Notification.builder()
//                .setTitle(title)
//                .setBody(body)
//                .build())
//            //.putAllData(notificationReqDto.getData())// title, body, image
//            .build();
//        return objectMapper.writeValueAsString(message);
//    }
//
//    private String getAccessToken() throws IOException {
//        String firebaseConfigPath = "firebase-service-account.json";
//
//        GoogleCredentials googleCredentials = GoogleCredentials
//            .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
//            .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
//
//        googleCredentials.refreshIfExpired();
//        return googleCredentials.getAccessToken().getTokenValue();
//    }
//}