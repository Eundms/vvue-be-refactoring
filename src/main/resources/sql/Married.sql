-- married
drop table if exists married;

create table married (
       id bigint not null auto_increment,
        married_day date,
        first_id bigint not null,
        picture_id bigint,
        second_id bigint not null,
        primary key (id)
);

alter table married
   add constraint married_first_id_user_id
   foreign key (first_id)
   references user (id);

alter table married
   add constraint married_picture_id_id
   foreign key (picture_id)
   references picture (id);