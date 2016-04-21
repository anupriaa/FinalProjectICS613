# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table image_entry (
  entry_id                  bigint not null,
  entry_type                varchar(255),
  timestamp                 varchar(255),
  email                     varchar(255),
  user_info_id              bigint,
  constraint pk_image_entry primary key (entry_id))
;

create table image_info (
  image_id                  bigint not null,
  image                     bytea,
  image_name                varchar(255),
  image_entry_id            bigint,
  entry_entry_id            bigint,
  constraint pk_image_info primary key (image_id))
;

create table keywords (
  id                        bigint not null,
  keyword                   varchar(255),
  keyword_entry_id          bigint,
  keyword_relevance         float,
  url_entry_entry_id        bigint,
  note_entry_entry_id       bigint,
  image_entry_entry_id      bigint,
  constraint pk_keywords primary key (id))
;

create table note_entry (
  entry_id                  bigint not null,
  entry_type                varchar(255),
  timestamp                 varchar(255),
  email                     varchar(255),
  user_info_id              bigint,
  constraint pk_note_entry primary key (entry_id))
;

create table note_info (
  note_id                   bigint not null,
  note                      varchar(255),
  note_title                varchar(255),
  note_entry_id             bigint,
  entry_entry_id            bigint,
  constraint pk_note_info primary key (note_id))
;

create table url_entry (
  entry_id                  bigint not null,
  entry_type                varchar(255),
  email                     varchar(255),
  user_info_id              bigint,
  constraint pk_url_entry primary key (entry_id))
;

create table url_info (
  url_id                    bigint not null,
  url_type                  varchar(255),
  url                       varchar(255),
  url_snippet               varchar(255),
  date                      varchar(255),
  time                      varchar(255),
  og_image_present          boolean,
  url_ogimage               varchar(255),
  url_entry_id              bigint,
  entry_entry_id            bigint,
  constraint pk_url_info primary key (url_id))
;

create table user_info (
  id                        bigint not null,
  email                     varchar(255),
  password                  varchar(255),
  first_name                varchar(255),
  last_name                 varchar(255),
  image                     bytea,
  constraint pk_user_info primary key (id))
;

create sequence image_entry_seq;

create sequence image_info_seq;

create sequence keywords_seq;

create sequence note_entry_seq;

create sequence note_info_seq;

create sequence url_entry_seq;

create sequence url_info_seq;

create sequence user_info_seq;

alter table image_entry add constraint fk_image_entry_userInfo_1 foreign key (user_info_id) references user_info (id);
create index ix_image_entry_userInfo_1 on image_entry (user_info_id);
alter table image_info add constraint fk_image_info_entry_2 foreign key (entry_entry_id) references image_entry (entry_id);
create index ix_image_info_entry_2 on image_info (entry_entry_id);
alter table keywords add constraint fk_keywords_urlEntry_3 foreign key (url_entry_entry_id) references url_entry (entry_id);
create index ix_keywords_urlEntry_3 on keywords (url_entry_entry_id);
alter table keywords add constraint fk_keywords_noteEntry_4 foreign key (note_entry_entry_id) references note_entry (entry_id);
create index ix_keywords_noteEntry_4 on keywords (note_entry_entry_id);
alter table keywords add constraint fk_keywords_imageEntry_5 foreign key (image_entry_entry_id) references image_entry (entry_id);
create index ix_keywords_imageEntry_5 on keywords (image_entry_entry_id);
alter table note_entry add constraint fk_note_entry_userInfo_6 foreign key (user_info_id) references user_info (id);
create index ix_note_entry_userInfo_6 on note_entry (user_info_id);
alter table note_info add constraint fk_note_info_entry_7 foreign key (entry_entry_id) references note_entry (entry_id);
create index ix_note_info_entry_7 on note_info (entry_entry_id);
alter table url_entry add constraint fk_url_entry_userInfo_8 foreign key (user_info_id) references user_info (id);
create index ix_url_entry_userInfo_8 on url_entry (user_info_id);
alter table url_info add constraint fk_url_info_entry_9 foreign key (entry_entry_id) references url_entry (entry_id);
create index ix_url_info_entry_9 on url_info (entry_entry_id);



# --- !Downs

drop table if exists image_entry cascade;

drop table if exists image_info cascade;

drop table if exists keywords cascade;

drop table if exists note_entry cascade;

drop table if exists note_info cascade;

drop table if exists url_entry cascade;

drop table if exists url_info cascade;

drop table if exists user_info cascade;

drop sequence if exists image_entry_seq;

drop sequence if exists image_info_seq;

drop sequence if exists keywords_seq;

drop sequence if exists note_entry_seq;

drop sequence if exists note_info_seq;

drop sequence if exists url_entry_seq;

drop sequence if exists url_info_seq;

drop sequence if exists user_info_seq;

