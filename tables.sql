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


-- add data to database

INSERT INTO "public"."groups" ("group_id", "group_name") VALUES (1, 'ПЗПИ-17-1');
INSERT INTO "public"."groups" ("group_id", "group_name") VALUES (2, 'ПЗПИ-17-2');
INSERT INTO "public"."groups" ("group_id", "group_name") VALUES (3, 'ИТКН-16-8');

INSERT INTO "public"."students" ("student_id", "group_id", "student_name", "student_additional_points") VALUES (1, 1, 'Иванов Иван Иванович', 0);
INSERT INTO "public"."students" ("student_id", "group_id", "student_name", "student_additional_points") VALUES (2, 1, 'Кириллов Кирилл Кириллович', 3.5);
INSERT INTO "public"."students" ("student_id", "group_id", "student_name", "student_additional_points") VALUES (3, 2, 'Петров Пётр Петрович', 7.8);
INSERT INTO "public"."students" ("student_id", "group_id", "student_name", "student_additional_points") VALUES (4, 3, 'Сидоров Сидор Сидорович', 1.4);

INSERT INTO "public"."subjects" ("subject_id", "subject_name") VALUES (1, 'Физика');
INSERT INTO "public"."subjects" ("subject_id", "subject_name") VALUES (2, 'Высшая математика');
INSERT INTO "public"."subjects" ("subject_id", "subject_name") VALUES (3, 'Основы права');

INSERT INTO "public"."learning" ("learning_id", "group_id", "subject_id", "learning_coefficient") VALUES (1, 1, 1, 5);
INSERT INTO "public"."learning" ("learning_id", "group_id", "subject_id", "learning_coefficient") VALUES (2, 2, 1, 5);
INSERT INTO "public"."learning" ("learning_id", "group_id", "subject_id", "learning_coefficient") VALUES (3, 1, 2, 6);
INSERT INTO "public"."learning" ("learning_id", "group_id", "subject_id", "learning_coefficient") VALUES (4, 2, 2, 6);
INSERT INTO "public"."learning" ("learning_id", "group_id", "subject_id", "learning_coefficient") VALUES (5, 3, 1, 8);
INSERT INTO "public"."learning" ("learning_id", "group_id", "subject_id", "learning_coefficient") VALUES (6, 3, 2, 7);
INSERT INTO "public"."learning" ("learning_id", "group_id", "subject_id", "learning_coefficient") VALUES (7, 3, 3, 3);

INSERT INTO "public"."progress" ("progress_id", "student_id", "learning_id", "progress_points") VALUES (1, 1, 1, 95);
INSERT INTO "public"."progress" ("progress_id", "student_id", "learning_id", "progress_points") VALUES (2, 1, 3, 91);
INSERT INTO "public"."progress" ("progress_id", "student_id", "learning_id", "progress_points") VALUES (3, 2, 2, 84);
INSERT INTO "public"."progress" ("progress_id", "student_id", "learning_id", "progress_points") VALUES (4, 2, 4, 71);
INSERT INTO "public"."progress" ("progress_id", "student_id", "learning_id", "progress_points") VALUES (5, 3, 5, 60);
INSERT INTO "public"."progress" ("progress_id", "student_id", "learning_id", "progress_points") VALUES (6, 3, 6, 65);
INSERT INTO "public"."progress" ("progress_id", "student_id", "learning_id", "progress_points") VALUES (7, 3, 7, 84);