package com.exciting.vvue.memory;

import static com.exciting.vvue.auth.oauth.model.OAuthProvider.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

import org.junit.jupiter.api.AfterEach;
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
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.exciting.vvue.auth.model.dto.AuthRes;
import com.exciting.vvue.auth.oauth.model.dto.SocialUserDto;
import com.exciting.vvue.memory.model.dto.req.MemoryAddReqDto;
import com.exciting.vvue.memory.model.dto.req.PlaceMemoryReqDto;
import com.exciting.vvue.memory.model.dto.res.ScheduleResDto;
import com.exciting.vvue.place.model.dto.PlaceReqDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yml")
@TestPropertySource("classpath:application.yml")
@AutoConfigureMockMvc
@Testcontainers
public class MemoryTest {
	@Container
	private static final MySQLContainer<?> mysqlContainer =
		new MySQLContainer<>("mysql:8.0.33")
			.withDatabaseName("testvvue")
			.withUsername("test")
			.withPassword("test")
			.waitingFor(Wait.forListeningPort());
	@Container
	public static final GenericContainer redisContainer =
		new GenericContainer(DockerImageName.parse("redis:latest"))
		.withExposedPorts(6379)
		.waitingFor(Wait.forListeningPort());;

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
		registry.add("spring.datasource.username", mysqlContainer::getUsername);
		registry.add("spring.datasource.password", mysqlContainer::getPassword);

		registry.add("spring.redis.host", redisContainer::getHost);
		registry.add("spring.redis.port", () -> redisContainer.getMappedPort(6379));
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private MockMvc mockMvc;

	private ObjectMapper objectMapper = new ObjectMapper();

	private List<PlaceReqDto> places;
	private Map<Long, AuthRes> authed;
	private List<ScheduleResDto> completed;

	@BeforeEach
	void setup() throws Exception {
		setupDatabase();
		setupLogin();
		setupSchedule();
		setUpPlaces();
	}

	@Test
	void 추억작성_테스트() throws Exception {
		AuthRes user1 = authed.get(1L);

		List<PlaceMemoryReqDto> placeMemories1 = new ArrayList<>();
		placeMemories1.add(new PlaceMemoryReqDto(
			places.get(0),
			5.0f,
			 "유저1_장소_커멘트",
			List.of(1L, 2L, 3L)
		));

		ScheduleResDto schedule = completed.get(0);
		MemoryAddReqDto user1Req = new MemoryAddReqDto(schedule.getId(), schedule.getName(), schedule.getDate(), "커멘트1", 1L, placeMemories1);

		ResultActions resultActions = mockMvc.perform(post("/memory")
			.contentType("application/json")
			.header("Authorization", user1.getAccessToken())
			.content(objectMapper.writeValueAsString(user1Req))
		);
		resultActions.andExpect(status().isOk());
	}



	void setupDatabase() throws IOException {
		String sqlFilePath = "src/test/resources/sql/regist_data.sql";

		String sql = Files.lines(Paths.get(sqlFilePath))
			.collect(Collectors.joining("\n"));

		for (String query : sql.split(";")) {
			if (!query.trim().isEmpty()) {
				jdbcTemplate.execute(query.trim());
			}
		}
	}

	void setupLogin() throws Exception {
		authed = new HashMap<>();
		authed.put(1L, sendLoginRequest(1L));
		authed.put(2L, sendLoginRequest(2L));
	}

	private AuthRes sendLoginRequest(Long id) throws Exception {
		Map<Long, SocialUserDto> maps = new HashMap<>();
		maps.put(1L, new SocialUserDto("a@naver.com", "a", KAKAO, "a"));
		maps.put(2L, new SocialUserDto("b@naver.com", "b", GOOGLE, "b"));

		SocialUserDto loginReq = maps.get(id);

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
		return objectMapper.readValue(responseBody, AuthRes.class);
	}
	private void setupSchedule() throws IOException {

		String sqlFilePath = "src/test/resources/sql/schedule_data.sql";

		String sql = Files.lines(Paths.get(sqlFilePath))
			.collect(Collectors.joining("\n"));

		jdbcTemplate.execute(sql);

		completed = List.of(
			new ScheduleResDto(1L, "결혼기념일", LocalDate.parse("2025-01-08")),
			new ScheduleResDto(2L, "남편생일", LocalDate.parse("1990-12-08")),
			new ScheduleResDto(3L, "아내생일", LocalDate.parse("1995-08-13")),
			new ScheduleResDto(4L,  "신년여행", LocalDate.parse("2024-01-01")),
			new ScheduleResDto(5L,  "매달 저녁 약속", LocalDate.parse("2024-08-08")),
			new ScheduleResDto(6L,  "남편생일", LocalDate.parse("1990-12-08"))
		);
	}

	private void setUpPlaces() throws Exception {
		places = objectMapper.readValue(
			new File("src/test/resources/places.json"), // JSON 파일 경로
			objectMapper.getTypeFactory().constructCollectionType(List.class, PlaceReqDto.class)
		);
	}

}
