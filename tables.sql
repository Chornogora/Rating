create database rating;

-- creating table groups

create table groups
(
	group_id BIGINT
		constraint groups_pk
			primary key,
	group_name varchar(20) not null
);

create unique index groups_group_name_uindex
	on groups (group_name);

-- creating table subjects

create table subjects
(
  subject_id bigint
    constraint subjects_pk
      primary key,
  subject_name varchar(50) not null
);

create unique index subjects_subject_name_uindex
  on subjects (subject_name);

-- create table learning

create table learning
(
  learning_id bigint
    constraint learning_pk
      primary key,
  group_id bigint not null
    constraint group___fk
      references groups,
  subject_id bigint not null
    constraint subject___fk
      references subjects,
  learning_coefficient double precision default 0 not null
);

-- create table students

create table students
(
  student_id bigint
    constraint students_pk
      primary key,
  group_id bigint not null
    constraint groups__fk
      references groups,
  student_name varchar(60) not null,
  student_additional_points double precision default 0 not null
);

-- create table progress

create table progress
(
  progress_id bigint
    constraint progress_pk
      primary key,
  student_id bigint not null
    constraint students___fk
      references students,
  learning_id bigint not null
    constraint learning___fk
      references learning,
  progress_points int default 0 not null
);