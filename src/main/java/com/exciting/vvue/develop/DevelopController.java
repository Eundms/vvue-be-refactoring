package com.exciting.vvue.develop;

import com.exciting.vvue.auth.AuthContext;
import com.exciting.vvue.auth.AuthService;
import com.exciting.vvue.auth.jwt.model.JwtDto;
import com.exciting.vvue.auth.model.Auth;
import com.exciting.vvue.common.dto.MessageResDto;
import com.exciting.vvue.married.MarriedService;
import com.exciting.vvue.married.dto.req.MarriedCreateDto;
import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.memory.MemoryService;
import com.exciting.vvue.memory.dto.req.MemoryAddReqDto;
import com.exciting.vvue.memory.dto.req.PlaceMemoryReqDto;
import com.exciting.vvue.notification.model.NotificationContent;
import com.exciting.vvue.notification.model.NotificationType;
import com.exciting.vvue.notification.model.VvueNotification;
import com.exciting.vvue.notification.service.VvueNotificationRepository;
import com.exciting.vvue.picture.model.Picture;
import com.exciting.vvue.picture.repository.PictureRepository;
import com.exciting.vvue.place.PlaceService;
import com.exciting.vvue.place.dto.req.PlaceDumpReqDto;
import com.exciting.vvue.place.dto.req.PlaceReqDto;
import com.exciting.vvue.schedule.ScheduleService;
import com.exciting.vvue.schedule.dto.req.ScheduleReqDto;
import com.exciting.vvue.schedule.dto.res.ScheduleResDto;
import com.exciting.vvue.schedule.model.RepeatCycle;
import com.exciting.vvue.schedule.model.Schedule;
import com.exciting.vvue.user.UserService;
import com.exciting.vvue.common.exception.ErrorCode;
import com.exciting.vvue.common.exception.VvueException;
import com.exciting.vvue.user.model.Gender;
import com.exciting.vvue.user.model.User;
import com.exciting.vvue.user.service.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DevelopController implements DevelopApi {

  private final UserService userService;
  private final AuthService authService;
  private final MarriedService marriedService;
  private final ScheduleService scheduleService;
  private final MemoryService memoryService;
  private final PlaceService placeService;
  private final PictureRepository pictureRepository;
  private final VvueNotificationRepository vvueNotificationRepository;
  private final UserRepository userRepository;
  @Value("${auth.kakao.client-id}")
  String api_key;

  @PostMapping("/force-marry/{spouseId}")
  @Override
  public MessageResDto forceMarry(@PathVariable Long spouseId) {
    Long userId = AuthContext.getUserId();

    User user = userService.getUserById(userId);
    User spouse = userService.getUserById(spouseId);
    if (user.getId() == spouse.getId()) {
      throw new VvueException(ErrorCode.BAD_REQUEST);
    }
    Married userMarriedInfo = marriedService.getMarriedByUserIdWithDetails(user.getId());
    Married spouseMarriedInfo = marriedService.getMarriedByUserIdWithDetails(spouse.getId());
    if (userMarriedInfo != null || spouseMarriedInfo != null) {
      throw new VvueException(ErrorCode.BAD_REQUEST);
    }
    marriedService.createMarried(userId,
        MarriedCreateDto.builder().partnerId(spouseId).build());
    return MessageResDto.of("결혼 요청:userId " + userId + " " + spouseId + " 결혼 완료");
  }

  @DeleteMapping("/divorce")
  @Override
  public MessageResDto divorce() {
    Long userId = AuthContext.getUserId();
    marriedService.deleteByUserId(userId);
    return MessageResDto.of("이혼 요청:userId " + userId + " 이혼 완료");
  }

  @Deprecated
  @PostMapping("/for-test")
  @Override
  public void testGenearte() {
    //TODO
    for (int i = 1; i <= 10; i++) {
      pictureRepository.save(Picture.builder().url(
              "https://vvue-bucket.s3.ap-northeast-2.amazonaws.com/image/2023/09/12/0c059ba8-fd0d-4610-a276-2189947f4eb0.jpg")
          .isDeleted(false).build());
    }
    for (long userId = 1; userId <= 8; userId++) {
      Long TEST_USER_ID = userId;
      String provider;
      Gender gender;
      if (userId % 2 == 0) {
        gender = Gender.MALE;
        provider = "google";
      } else {
        gender = Gender.FEMALE;
        provider = "kakao";
      }
      User testUser = User.builder()
          .id(TEST_USER_ID)
          .nickname("nick_" + userId)
          .email("test" + userId + "@gmail.com")
          .provider(provider)
          .providerId("" + userId)
          .birthday(
              LocalDate.of(1990 + (int) userId * 10, (10 + (int) userId) % 12 + 1, 3))
          .gender(gender)
          .build();
      userRepository.save(testUser);
    }
    long[] pair = {2L, 4L, 6L};
    for (int i = 0; i < 3; i++) {// 1 3 5
      LocalDate marriedDate = LocalDate.now().minusYears((long) (i + 1) * 2).minusDays((i + 1) * 3);
      marriedService.createMarried(pair[i] - 1, new MarriedCreateDto(pair[i], marriedDate));
    }

    ObjectMapper objectMapper = new ObjectMapper();

    for (int i = 0; i < 3; i++) {
      Married married = marriedService.getMarriedByUserIdWithDetails(pair[i] - 1);
      LocalDate marriedDate = married.getMarriedDay();

      scheduleService.addSchedule(married,
          new ScheduleReqDto(marriedDate, "결혼기념일", RepeatCycle.YEARLY));

      Map<String, String> data = new HashMap<>();
      data.put("scheduleDate", "2022-09-09");
      String value = null;
      try {
        value = objectMapper.writeValueAsString(data);
      } catch (IOException ex) {
        log.debug(ex.getMessage());
      }
      vvueNotificationRepository.save(
          VvueNotification.builder()
              .receiverId(pair[i]).content(
                  NotificationContent.builder().title("결혼기념일").body("결혼기념일입니다!").image(null)
                      .build())
              .notificationType(NotificationType.MARRIED)
              .isRead(false)
              //.data(value)
              .build());
      vvueNotificationRepository.save(VvueNotification.builder()
          .receiverId(pair[i] - 1).content(
              NotificationContent.builder().title("결혼기념일").body("결혼기념일입니다!").image(null)
                  .build())
          .notificationType(NotificationType.MARRIED)
          .isRead(false)
          //.data(value)
          .build());

      LocalDate montlyDate = LocalDate.now().plusDays(i);

      scheduleService.addSchedule(married,
          new ScheduleReqDto(montlyDate, "일반일정(달반복)", RepeatCycle.MONTHLY));
      vvueNotificationRepository.save(VvueNotification.builder()
          .receiverId(pair[i]).content(
              NotificationContent.builder().title("일반일정(달반복)").body("일반일정(달반복)!").image(null)
                  .build())
          .notificationType(NotificationType.SCHEDULE)
          //.data(value)
          .isRead(false)
          .build());

      vvueNotificationRepository.save(VvueNotification.builder()
          .receiverId(pair[i] - 1).content(
              NotificationContent.builder().title("일반일정(달반복)").body("일반일정(달반복)!").image(null)
                  .build())
          .notificationType(NotificationType.SCHEDULE)
          //.data(value)
          .isRead(false)
          .build());

      LocalDate date = LocalDate.now().plusDays(i);

      scheduleService.addSchedule(married,
          new ScheduleReqDto(date, "일반일정(반복없음)", RepeatCycle.NONREPEAT));
      vvueNotificationRepository.save(VvueNotification.builder()
          .receiverId(pair[i]).content(
              NotificationContent.builder().title("일반일정(반복없음)").body("일반일정(반복없음)!")
                  .image(null).build())
          .notificationType(NotificationType.SCHEDULE)
          //.data(value)
          .isRead(false)
          .build());
      vvueNotificationRepository.save(VvueNotification.builder()
          .receiverId(pair[i] - 1).content(
              NotificationContent.builder().title("일반일정(반복없음)").body("일반일정(반복없음)!")
                  .image(null).build())
          .notificationType(NotificationType.SCHEDULE)
          //.data(value)
          .isRead(false)
          .build());
    }

    Map<String, String> data2 = new HashMap<>();
    data2.put("memoryId", "1");
    String value2 = null;
    try {
      value2 = objectMapper.writeValueAsString(data2);
    } catch (IOException ex) {
      log.debug(ex.getMessage());
    }
    User spouse = userService.getUserById(2L);
    vvueNotificationRepository.save(VvueNotification.builder()
        .receiverId(1L).content(
            NotificationContent.builder().title("후기").body("후기가 등록되었습니다! 확인해보세요!!!")
                .image(spouse.getPicture() == null ? null : spouse.getPicture().getUrl())
                .build())
        .notificationType(NotificationType.MEMORY)
        .isRead(false)
        //.data(value2)
        .build());

    for (int i = 0; i < 3; i++) {
      User user = userService.getUserById(pair[i]);
      Married married = marriedService.getMarriedByUserIdWithDetails(pair[i]);

      List<ScheduleResDto> schedule = scheduleService.getAllSchedule(married, -1, 100)
          .getScheduleResDtoList();
      for (int j = 0; j < schedule.size(); j++) {
        MemoryAddReqDto memory2 = MemoryAddReqDto.builder()
            .scheduleId(schedule.get(j).getId())
            .scheduleDate(schedule.get(j).getScheduleDate())
            .scheduleName(schedule.get(j).getScheduleName())
            .comment("코멘트" + i)
            .pictureId(pair[i] - 1)
            .placeMemories(List.of(
                new PlaceMemoryReqDto(
                    PlaceReqDto.builder()
                        .id(Long.toString(i * 1000 + j))
                        .address_name("질문")
                        .category_group_code("")
                        .category_group_name("")
                        .distance("1")
                        .phone("010-9999-9999")
                        .place_name("어딜가든 역삼")
                        .place_url("URL")
                        .road_address_name("역삼로")
                        .x("1")
                        .y("2")
                        .build()
                    , 2f, "COMMENT" + i, List.of(1L, 2L, 3L)),
                new PlaceMemoryReqDto(
                    PlaceReqDto.builder()
                        .id(Long.toString(i * 100 + j))
                        .address_name("질문2")
                        .category_group_code("")
                        .category_group_name("")
                        .distance("2")
                        .phone("010-2222-2222")
                        .place_name("어딜가든 선릉")
                        .place_url("URL")
                        .road_address_name("선릉로")
                        .x("1")
                        .y("2")
                        .build()
                    , 5.0f, "COMMENT" + i, List.of(4L, 5L, 6L))
            ))
            .build();
        memoryService.add(memory2, user, married);
      }
    }
  }

  @GetMapping("/test-token-gen/{providerId}")
  @Override
  public JwtDto testTokenGen(@PathVariable Long providerId) {
    Long TEST_USER_ID = providerId;
    Optional<User> testUser = userRepository.findById(TEST_USER_ID);
    if (testUser.isEmpty()) {
      testUser = Optional.ofNullable(User.builder()
          .email("test" + providerId + "@gmail.com")
          .createdAt(LocalDateTime.now())
          .birthday(
              LocalDate.of(1990 + providerId.intValue() * 10,
                  (10 + providerId.intValue()) % 12 + 1,
                  3))
          .provider("google")
          .providerId("" + providerId)
          .nickname("nick_" + providerId)
          .gender(Gender.MALE)
          .build());
      testUser = Optional.of(userRepository.save(testUser.get()));
    }

    JwtDto jwtDto = authService.createTokens(testUser.get());
    Auth authEntity = authService.getSavedTokenByUserId(testUser.get().getId());
    if (authEntity == null) {
      authService.saveTokens(jwtDto);
    } else {
      authEntity.setRefreshToken(jwtDto.getRefreshToken());
      authService.updateTokens(authEntity);
    }
    return jwtDto;
  }

  @PostMapping("/dump-place-data")
  @Override
  public MessageResDto dumpPlaceData(@RequestHeader("authUserId") Long userId) {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new VvueException(ErrorCode.USER_NOT_FOUND)
    );
    Married married = marriedService.getMarriedByUserIdWithDetails(userId);
    Picture picture = pictureRepository.save(Picture.builder()
        .url(
            "https://vvue-bucket.s3.ap-northeast-2.amazonaws.com/image/2023/09/12/0c059ba8-fd0d-4610-a276-2189947f4eb0.jpg")
        .isDeleted(false)
        .build());

    try {
      String[] codes = {"CT1", "AT4", "FD6", "CE7"};

      for (String code : codes) {
        ObjectMapper objectMapper = new ObjectMapper();

        String json = requestToKakaoMap(code);

        PlaceDumpReqDto placeDumpReqDto = objectMapper.readValue(json, PlaceDumpReqDto.class);

        List<PlaceReqDto> placeReqDtoList = placeDumpReqDto.getDocuments();

        for (PlaceReqDto placeReqDto : placeReqDtoList) {
          Schedule schedule = scheduleService.addSchedule(married,
              new ScheduleReqDto(LocalDate.parse("2023-01-01"), "더미플레이스용", RepeatCycle.NONREPEAT));
          placeService.addPlace(placeReqDto);
          List<PlaceMemoryReqDto> placeMemoryReqDtoList = new ArrayList<>();
          Random random = new Random();
          for (int i = 0; i < 10; i++) {
            placeMemoryReqDtoList.add(
                new PlaceMemoryReqDto(placeReqDto, (float) (random.nextInt(10)) / 2, "",
                    Arrays.asList(picture.getId())));
          }
          memoryService.add(MemoryAddReqDto.builder()
              .pictureId(picture.getId())
              .scheduleId(schedule.getId())
              .comment("더미코멘트")
              .placeMemories(placeMemoryReqDtoList)
              .build(), user, married);
        }
      }
    } catch (JsonProcessingException e) {
      throw new VvueException(ErrorCode.BAD_REQUEST);
    }

    return MessageResDto.of("덤프 데이터 생성 완료");
  }

  private String requestToKakaoMap(String code) {
    RestTemplate restTemplate = new RestTemplate();
    // REST 요청의 헤더에 들어갈 내용 저장
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "KakaoAK " + api_key);

    Random random = new Random();
    Double x = 126.5 + 2.5 * random.nextDouble();
    Double y = 34.5 + 3.2 * random.nextDouble();

    // headers and parameters로 request entity 생성
    HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    // API endpoint URL 설정
    String url = "https://dapi.kakao.com/v2/local/search/category.JSON";
    UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
        .queryParam("category_group_code", code)
        .queryParam("x", x.toString())
        .queryParam("y", y.toString())
        .queryParam("radius", "10000")
        .build(true);

    // 카카오 서버로 GET 요청 보내기
    ResponseEntity<String> responseEntity = restTemplate.exchange(uriBuilder.toString(),
        HttpMethod.GET,
        requestEntity, String.class);

    return responseEntity.getBody();
  }

  @PostMapping("/dump-place-data-param")
  @Override
  public MessageResDto selectDumpPlaceData(@RequestHeader("authUserId") Long userId,
      @RequestParam("x") Double x,
      @RequestParam("y") Double y, @RequestParam("code") String categoryCode) {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new VvueException(ErrorCode.USER_NOT_FOUND)
    );
    Married married = marriedService.getMarriedByUserIdWithDetails(userId);
    Picture picture = pictureRepository.save(Picture.builder()
        .url(
            "https://vvue-bucket.s3.ap-northeast-2.amazonaws.com/image/2023/09/12/0c059ba8-fd0d-4610-a276-2189947f4eb0.jpg")
        .isDeleted(false)
        .build());

    try {
      ObjectMapper objectMapper = new ObjectMapper();

      String json = requestToKakaoMapByParams(categoryCode, x, y);

      PlaceDumpReqDto placeDumpReqDto = objectMapper.readValue(json, PlaceDumpReqDto.class);

      List<PlaceReqDto> placeReqDtoList = placeDumpReqDto.getDocuments();

      for (PlaceReqDto placeReqDto : placeReqDtoList) {
        Schedule schedule = scheduleService.addSchedule(married,
            new ScheduleReqDto(LocalDate.parse("2023-01-01"), "더미플레이스용", RepeatCycle.NONREPEAT));
        placeService.addPlace(placeReqDto);
        List<PlaceMemoryReqDto> placeMemoryReqDtoList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
          placeMemoryReqDtoList.add(
              new PlaceMemoryReqDto(placeReqDto, (float) (random.nextInt(10)) / 2, "",
                  Arrays.asList(picture.getId())));
        }
        memoryService.add(MemoryAddReqDto.builder()
            .pictureId(picture.getId())
            .scheduleId(schedule.getId())
            .scheduleName(schedule.getScheduleName())
            .scheduleDate(schedule.getScheduleDate().toString())
            .comment("더미코멘트")
            .placeMemories(placeMemoryReqDtoList)
            .build(), user, married);
      }
    } catch (JsonProcessingException e) {
      throw new VvueException(ErrorCode.BAD_REQUEST);
    }

    return MessageResDto.of("위치, 코드 지정 덤프 데이터 생성 완료");
  }

  private String requestToKakaoMapByParams(String code, Double x, Double y) {
    RestTemplate restTemplate = new RestTemplate();
    // REST 요청의 헤더에 들어갈 내용 저장
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "KakaoAK " + api_key);

    // headers and parameters로 request entity 생성
    HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    // API endpoint URL 설정
    String url = "https://dapi.kakao.com/v2/local/search/category.JSON";
    UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
        .queryParam("category_group_code", code)
        .queryParam("x", x.toString())
        .queryParam("y", y.toString())
        .queryParam("radius", "10000")
        .build(true);

    // 카카오 서버로 GET 요청 보내기
    ResponseEntity<String> responseEntity = restTemplate.exchange(uriBuilder.toString(),
        HttpMethod.GET,
        requestEntity, String.class);

    return responseEntity.getBody();
  }
}
