package com.exciting.vvue.married;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import com.exciting.vvue.married.dto.MarriedModifyDto;
import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.married.service.MarriedRepository;
import com.exciting.vvue.picture.repository.PictureRepository;
import com.exciting.vvue.user.service.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yml")
@TestPropertySource("classpath:application.yml")
@Testcontainers
public class MarriedServiceConcurrencyTest {
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
			.waitingFor(Wait.forListeningPort());

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
	private MarriedService marriedService;

	@Autowired
	private MarriedRepository marriedRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PictureRepository pictureRepository;
	@BeforeEach
	void setUp() throws Exception{
		setupDatabase();
	}
	@Test
	@Transactional
	void testConcurrencyOnUpdateMarried() throws InterruptedException {
		Long userId = 1L; // 테스트 대상 유저 ID
		MarriedModifyDto modifyDto1 = new MarriedModifyDto(LocalDate.of(2025, 1, 1), 1L);
		MarriedModifyDto modifyDto2 = new MarriedModifyDto(LocalDate.of(2025, 2, 2), 2L);

		int threadCount = 2;
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		CountDownLatch latch = new CountDownLatch(threadCount);

		executorService.execute(() -> {
			try {
				marriedService.updateMarriedAndReturnId(userId, modifyDto1);
			} finally {
				latch.countDown();
			}
		});

		executorService.execute(() -> {
			try {
				marriedService.updateMarriedAndReturnId(userId, modifyDto2);
			} finally {
				latch.countDown();
			}
		});

		latch.await();

		Married married = marriedRepository.findByUserIdWithDetails(userId);

		System.out.println("최종 Married 상태: " + married.getMarriedDay());
		assertThat(married).isNotNull();
	}

	@Test
	@Transactional
	void testConcurrencyFromPeers() throws InterruptedException {
		Long firstUserId = 1L;
		Long secondUserId = 2L;

		MarriedModifyDto firstUserDto = new MarriedModifyDto(LocalDate.of(2025, 1, 1), 2L);
		MarriedModifyDto secondUserDto = new MarriedModifyDto(LocalDate.of(2025, 2, 2), 3L);

		int threadCount = 2;
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		CountDownLatch latch = new CountDownLatch(threadCount);

		executorService.execute(() -> {
			try {
				marriedService.updateMarriedAndReturnId(firstUserId, firstUserDto);
			} finally {
				latch.countDown();
			}
		});

		executorService.execute(() -> {
			try {
				marriedService.updateMarriedAndReturnId(secondUserId, secondUserDto);
			} finally {
				latch.countDown();
			}
		});

		latch.await();

		Married married = marriedRepository.findByUserIdWithDetails(firstUserId);
		System.out.println("PEERS : 최종 Married 상태: " + married);

		assertThat(married).isNotNull();
	}

	void setupDatabase() throws IOException {
		String[] sqlFilePath = new String[]{
			"src/test/resources/sql/0_picture.sql",
			"src/test/resources/sql/1_regist_data.sql",
			"src/test/resources/sql/2_marry.sql",
		};

		for(String sqlFile : sqlFilePath) {
			String sql = Files.lines(Paths.get(sqlFile))
				.collect(Collectors.joining("\n"));

			for (String query : sql.split(";")) {
				if (!query.trim().isEmpty()) {
					jdbcTemplate.execute(query.trim());
				}
			}
		}

	}
}
