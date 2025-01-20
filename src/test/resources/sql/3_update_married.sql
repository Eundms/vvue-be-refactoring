UPDATE testvvue.married
SET married_day = '2024-01-08',
    picture_id = 1
WHERE id = 1;

INSERT INTO testvvue.schedule (id, date_type, repeat_cycle, schedule_date, schedule_name, married_id) VALUES
(1, 'WEDDINGANNIVERSARY', 'YEARLY', '2025-01-08', '결혼기념일', 1),
(2, 'MALEBIRTHDAY', 'YEARLY', '1990-12-08', '남편생일', 1),
(3, 'FEMALEBIRTHDAY', 'YEARLY', '1995-08-13', '아내생일', 1);