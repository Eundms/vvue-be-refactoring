-- favorite_place
drop table if exists favorite_place;

create table favorite_place (
    id bigint not null auto_increment,
    place_id bigint,
    user_id bigint,
    primary key (id)
);

alter table favorite_place
   add constraint favorite_place_id_place_id
   foreign key (place_id)
   references place (id);

alter table favorite_place
   add constraint favorite_place_id_user_id
   foreign key (user_id)
   references user (id);

-- place
drop table if exists place;

create table place (
       id bigint not null,
        address_name varchar(255),
        category_group_code varchar(255),
        category_group_name varchar(255),
        category_name varchar(255),
        phone varchar(255),
        place_name varchar(255),
        place_url varchar(255),
        road_address_name varchar(255),
        x decimal(20,17),
        y decimal(20,17),
        primary key (id)
    );

-- place_stats
drop table if exists place_stats;

create table place_stats (
    id bigint not null,
    avg_rating double precision not null,
    visit_count bigint not null,
    primary key (id)
);