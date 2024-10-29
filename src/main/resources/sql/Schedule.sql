-- schedule
drop table if exists schedule;

create table schedule (
       id bigint not null auto_increment,
        date_type char(20),
        repeat_cycle char(12),
        schedule_date date,
        schedule_name varchar(60),
        married_id bigint,
        primary key (id)
);

