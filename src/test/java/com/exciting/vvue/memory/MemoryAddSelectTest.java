package com.exciting.vvue.memory;

import static com.exciting.vvue.auth.model.OAuthProvider.GOOGLE;
import static com.exciting.vvue.auth.model.OAuthProvider.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.exciting.vvue.auth.dto.req.SocialUserReqDto;
import com.exciting.vvue.auth.dto.res.AuthResDto;
import com.exciting.vvue.memory.dto.req.MemoryAddReqDto;
import com.exciting.vvue.memory.dto.req.PlaceMemoryReqDto;
import com.exciting.vvue.memory.dto.res.MemoryResDto;
import com.exciting.vvue.memory.dto.res.PlaceCommentResDto;
import com.exciting.vvue.memory.dto.res.PlaceMemoryResDto;
import com.exciting.vvue.memory.dto.res.ScheduleResDto;
import com.exciting.vvue.memory.dto.res.UserMemoryResDto;
import com.exciting.vvue.place.dto.req.PlaceReqDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yml")
@TestPropertySource("classpath:application.yml")
@AutoConfigureMockMvc
@Testcontainers
public class MemoryAddSelectTest {

  @Container
  public static final GenericContainer redisContainer =
      new GenericContainer(DockerImageName.parse("redis:latest"))
          .withExposedPorts(6379)
          .waitingFor(Wait.forListeningPort());
  @Container
  private static final MySQLContainer<?> mysqlContainer =
      new MySQLContainer<>("mysql:8.0.33")
          .withDatabaseName("testvvue")
          .withUsername("test")
          .withPassword("test")
          .waitingFor(Wait.forListeningPort());
  @Autowired
  private JdbcTemplate jdbcTemplate;
  @Autowired
  private MockMvc mockMvc;
  private ObjectMapper objectMapper = new ObjectMapper();
  private List<PlaceReqDto> places;
  private Map<Long, AuthResDto> authed;
  private List<ScheduleResDto> completed;

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
    registry.add("spring.datasource.username", mysqlContainer::getUsername);
    registry.add("spring.datasource.password", mysqlContainer::getPassword);

    registry.add("spring.redis.host", redisContainer::getHost);
    registry.add("spring.redis.port", () -> redisContainer.getMappedPort(6379));
  }

  @BeforeEach
  void setup() throws Exception {
    setupDatabase();
    setupLogin();
    setupSchedule();
    setUpPlaces();
  }

  @Test
  void 추억작성_테스트() throws Exception {

    MemoryAddReqDto reqDto = prepareReqMemory(1L, 0, 0);

    addMemoryTest(1L, reqDto);

    MemoryResDto resDto = getMemoryTest(1L, 1L);
    assertAll(
        () -> {
          assertThat(resDto.getScheduleInfo().getName()).isEqualTo(reqDto.getScheduleName());
          assertThat(resDto.getScheduleInfo().getDate()).isEqualTo(reqDto.getScheduleDate());
        }
    );


  }

  MemoryAddReqDto prepareReqMemory(Long pictureId, int placeIdx, int scheduleIdx) {
    List<PlaceMemoryReqDto> placeMemories1 = new ArrayList<>();
    placeMemories1.add(new PlaceMemoryReqDto(
        places.get(placeIdx),
        5.0f,
        "유저1_장소_커멘트",
        List.of(1L, 2L, 3L)
    ));

    ScheduleResDto schedule = completed.get(scheduleIdx);
    return new MemoryAddReqDto(schedule.getId(), schedule.getName(), schedule.getDate(), "커멘트1",
        pictureId, placeMemories1);
  }

  void addMemoryTest(Long userId, MemoryAddReqDto user1Req) throws Exception {
    AuthResDto user1 = authed.get(userId);

    ResultActions resultActions = mockMvc.perform(post("/memory")
        .contentType("application/json")
        .header("Authorization", user1.getAccessToken())
        .content(objectMapper.writeValueAsString(user1Req))
    );
    resultActions.andExpect(status().isOk());
  }


  MemoryResDto getMemoryTest(Long memoryId, Long userId) throws Exception {
    AuthResDto user1 = authed.get(userId);

    ResultActions resultActions = mockMvc.perform(get("/memory/" + memoryId)
        .header("Authorization", user1.getAccessToken())
    );

    // 응답을 DTO로 변환
    String jsonResponse = resultActions.andReturn().getResponse().getContentAsString();
    MemoryResDto memoryResDto = objectMapper.readValue(jsonResponse, MemoryResDto.class);

    // DTO 필드 값 검증
    assertThat(memoryResDto).isNotNull();
    assertThat(memoryId).isEqualTo(memoryResDto.getId()); // MemoryResDto의 id 검증

    // ScheduleInfo 검증
    assertThat(memoryResDto.getScheduleInfo()).isNotNull();
    assertThat(memoryResDto.getScheduleInfo().getId()).isNotNull();
    assertThat(memoryResDto.getScheduleInfo().getName()).isNotNull();
    assertThat(memoryResDto.getScheduleInfo().getDate()).isNotNull();

    // userMemories 검증
    assertThat(memoryResDto.getUserMemories()).isNotNull();
    assertThat(memoryResDto.getUserMemories().isEmpty()).isFalse();

    UserMemoryResDto userMemoryResDto = memoryResDto.getUserMemories().get(0);
    assertThat(userMemoryResDto.getId()).isNotNull();
    assertThat(userMemoryResDto.getComment()).isNotNull();
    assertThat(userMemoryResDto.getPicture()).isNotNull();
    assertThat(userMemoryResDto.getUser()).isNotNull();
    assertThat(userMemoryResDto.getUser().getId()).isNotNull();
    assertThat(userMemoryResDto.getUser().getNickname()).isNotNull();
    assertThat(userMemoryResDto.getUser().getPicture()).isNotNull();

    // placeMemories 검증
    assertThat(memoryResDto.getPlaceMemories()).isNotNull();
    assertThat(memoryResDto.getPlaceMemories().isEmpty()).isFalse();

    PlaceMemoryResDto placeMemoryResDto = memoryResDto.getPlaceMemories().get(0);
    assertThat(placeMemoryResDto.getPlace()).isNotNull();
    assertThat(placeMemoryResDto.getAllRating()).isNotNull();
    assertThat(placeMemoryResDto.getPictures()).isNotNull();
    assertThat(placeMemoryResDto.getPictures().isEmpty()).isFalse();
    assertThat(placeMemoryResDto.getComments()).isNotNull();

    PlaceCommentResDto placeCommentResDto = placeMemoryResDto.getComments().get(0);
    assertThat(placeCommentResDto.getId()).isNotNull();
    assertThat(placeCommentResDto.getRating()).isNotNull();
    assertThat(placeCommentResDto.getComment()).isNotNull();
    assertThat(placeCommentResDto.getUser()).isNotNull();
    assertThat(placeCommentResDto.getUser().getId()).isNotNull();
    assertThat(placeCommentResDto.getUser().getNickname()).isNotNull();
    assertThat(placeCommentResDto.getUser().getPicture()).isNotNull();
    return memoryResDto;
  }

  void setupDatabase() throws IOException {
    String[] sqlFilePath = new String[]{"src/test/resources/sql/1_regist_data.sql",
        "src/test/resources/sql/2_marry.sql",
        "src/test/resources/sql/3_update_married.sql",
        "src/test/resources/sql/4_add_default_schedule.sql"
    };

    for (String sqlFile : sqlFilePath) {
      String sql = Files.lines(Paths.get(sqlFile))
          .collect(Collectors.joining("\n"));

      for (String query : sql.split(";")) {
        if (!query.trim().isEmpty()) {
          jdbcTemplate.execute(query.trim());
        }
      }
    }

  }

  void setupLogin() throws Exception {
    authed = new HashMap<>();
    authed.put(1L, sendLoginRequest(1L));
    authed.put(2L, sendLoginRequest(2L));
  }

  private AuthResDto sendLoginRequest(Long id) throws Exception {
    Map<Long, SocialUserReqDto> maps = new HashMap<>();
    maps.put(1L, new SocialUserReqDto("a@naver.com", "a", KAKAO, "a"));
    maps.put(2L, new SocialUserReqDto("b@naver.com", "b", GOOGLE, "b"));

    SocialUserReqDto loginReq = maps.get(id);

    ResultActions resultActions = mockMvc.perform(post("/auth")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(loginReq))
    );

    resultActions.andExpect(status().isOk())
        .andExpect(jsonPath("$.userId").exists())
        .andExpect(jsonPath("$.refreshToken").exists())
        .andExpect(jsonPath("$.accessToken").exists())
        .andExpect(jsonPath("$.stage").exists());

    MvcResult mvcResult = resultActions.andReturn();
    String responseBody = mvcResult.getResponse().getContentAsString();
    return objectMapper.readValue(responseBody, AuthResDto.class);
  }

  private void setupSchedule() throws IOException {

    String sqlFilePath = "src/test/resources/sql/4_add_default_schedule.sql";

    String sql = Files.lines(Paths.get(sqlFilePath))
        .collect(Collectors.joining("\n"));

    jdbcTemplate.execute(sql);

    completed = List.of(
        new ScheduleResDto(1L, "결혼기념일", LocalDate.parse("2025-01-08")),
        new ScheduleResDto(2L, "남편생일", LocalDate.parse("1990-12-08")),
        new ScheduleResDto(3L, "아내생일", LocalDate.parse("1995-08-13")),
        new ScheduleResDto(4L, "신년여행", LocalDate.parse("2024-01-01")),
        new ScheduleResDto(5L, "매달 저녁 약속", LocalDate.parse("2024-08-08")),
        new ScheduleResDto(6L, "남편생일", LocalDate.parse("1990-12-08"))
    );
  }

  private void setUpPlaces() throws Exception {
    places = objectMapper.readValue(
        new File("src/test/resources/places.json"), // JSON 파일 경로
        objectMapper.getTypeFactory().constructCollectionType(List.class, PlaceReqDto.class)
    );
  }

}
