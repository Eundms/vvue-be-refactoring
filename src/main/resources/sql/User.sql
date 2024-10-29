-- user
drop table if exists user;

create table user (
       id bigint not null auto_increment,
        birthday date,
        created_at datetime,
        email varchar(255),
        password varchar(255),
        gender varchar(255),
        is_authenticated bit not null,
        modified_at datetime,
        nickname varchar(255),
        picture_id bigint,
        primary key (id)
    )