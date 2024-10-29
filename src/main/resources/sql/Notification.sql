-- notification
drop table if exists vvue_notification;
create table vvue_notification (
       id bigint not null auto_increment,
        body varchar(255),
        image varchar(255),
        title varchar(255),
        created_at datetime,
        data varchar(255),
        is_read bit,
        notification_type varchar(255) not null,
        receiver_id bigint,
        primary key (id)
);
--subscriber
drop table if exists subscriber;
create table subscriber (
       id bigint not null auto_increment,
        firebase_token varchar(255),
        user_id bigint,
        primary key (id)
);