package com.exciting.vvue.memory;

import static com.exciting.vvue.auth.model.OAuthProvider.*;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

import com.exciting.vvue.auth.dto.res.AuthResDto;
import com.exciting.vvue.auth.dto.req.SocialUserReqDto;
import com.exciting.vvue.memory.dto.req.MemoryAddReqDto;
import com.exciting.vvue.memory.dto.req.PlaceMemoryReqDto;
import com.exciting.vvue.memory.dto.res.ScheduleResDto;
import com.exciting.vvue.place.dto.req.PlaceReqDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yml")
@TestPropertySource("classpath:application.yml")
@AutoConfigureMockMvc
@Testcontainers
public class MemoryConcurrencyTest {
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
	private Map<Long, AuthResDto> authed;
	private List<ScheduleResDto> completed;

	@BeforeEach
	void setup() throws Exception {
		setupDatabase();
		setupLogin();
		setupSchedule();
		setUpPlaces();
	}

	@Test // TODO
	void test() {
		MemoryAddReqDto reqDto = prepareReqMemory(1L, 0, 0);

		ExecutorService executorService = Executors.newFixedThreadPool(2);

		// 두 스레드에서 동시에 addMemoryTest 호출
		executorService.submit(() -> {
			try {
				addMemoryTest(1L, reqDto);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		executorService.submit(() -> {
			try {
				addMemoryTest(2L, reqDto);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});


		executorService.shutdown();
		while (!executorService.isTerminated()) {

		}

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
		return new MemoryAddReqDto(schedule.getId(), schedule.getName(), schedule.getDate(), "커멘트1", pictureId, placeMemories1);
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
