-- picture
drop table if exists picture;

create table picture (
    id bigint not null auto_increment,
    is_deleted bit not null,
    url varchar(255),
    primary key (id)
);