-- 첫 번째 테이블 데이터 삽입
INSERT INTO testvvue.picture (is_deleted, url) VALUES
	(0, 'https://vvue-s3.s3.us-east-1.amazonaws.com/image/2025/01/08/6ea53470-1c67-4c78-8ad1-fd8691dc747d.png'),
	(0, 'https://vvue-s3.s3.us-east-1.amazonaws.com/image/2025/01/08/ec7ae23f-5e0e-4639-b31e-fecc52d60a08.jpeg'),
	(0, 'https://vvue-s3.s3.us-east-1.amazonaws.com/image/2025/01/08/0f73ee88-1ab3-4a18-97c7-b10a56c6b9c4.jpeg'),
	(0, 'https://vvue-s3.s3.us-east-1.amazonaws.com/image/2025/01/08/5b227475-8d52-4c65-a471-bcc8124209bc.png'),
	(0, 'https://vvue-s3.s3.us-east-1.amazonaws.com/image/2025/01/08/d5d3b89d-e96e-4082-87f2-69bd513698cd.jpeg'),
	(0, 'https://vvue-s3.s3.us-east-1.amazonaws.com/image/2025/01/02/0e3e4ab0-dc13-4c86-9f2f-a9b66fa90b32.png');

-- 두 번째 테이블 데이터 삽입
INSERT INTO testvvue.`user` (birthday, created_at, email, gender, is_authenticated, modified_at, nickname, provider, provider_id, picture_id) VALUES
	('1990-12-08', '2025-01-08 09:59:46', 'a@naver.com', 'MALE', 1, '2025-01-08 10:00:11', 'a', 'KAKAO', 'a', 1),
	('1995-08-13', '2025-01-08 10:00:33', 'b@gmail.com', 'FEMALE', 1, '2025-01-08 10:00:49', 'b', 'GOOGLE', 'b', 2);

-- 세 번째 테이블 데이터 삽입
INSERT INTO testvvue.married (id, married_day, first_id, picture_id, second_id) VALUES
(1, '2024-01-08', 1, 1, 2);

-- 네 번째 테이블 데이터 삽입
INSERT INTO testvvue.schedule (id, date_type, repeat_cycle, schedule_date, schedule_name, married_id) VALUES
(1, 'WEDDINGANNIVERSARY', 'YEARLY', '2025-01-08', '결혼기념일', 1),
(2, 'MALEBIRTHDAY', 'YEARLY', '1990-12-08', '남편생일', 1),
(3, 'FEMALEBIRTHDAY', 'YEARLY', '1995-08-13', '아내생일', 1);
