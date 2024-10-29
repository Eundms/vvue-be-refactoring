-- schedulememory
drop table if exists schedulememory;

create table schedulememory (
    id bigint not null auto_increment,
    created_at datetime,
    schedule_date date,
    schedule_id bigint,
    schedule_name varchar(255),
    married_id bigint not null,
    primary key (id)
);

-- usermemory
drop table if exists usermemory;
create table usermemory (
       id bigint not null auto_increment,
        comment varchar(255),
        picture_id bigint,
        schedule_memory_id bigint,
        user_id bigint,
        primary key (id)
) ;

-- placememory
drop table if exists placememory;
create table placememory (
    id bigint not null auto_increment,
    comment varchar(255),
    created_at datetime,
    rating float,
    place_id bigint,
    schedule_memory_id bigint,
    user_id bigint,
    primary key (id)
);

drop table if exists placememory_image;
create table placememory_image (
    id bigint not null auto_increment,
    picture_id bigint not null,
    placememory_id bigint not null,
    primary key (id)
);


