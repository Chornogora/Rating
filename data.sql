-- add data to database

INSERT INTO "public"."groups" ("group_id", "group_name") VALUES (1, 'ПЗПИ-17-1');
INSERT INTO "public"."groups" ("group_id", "group_name") VALUES (2, 'ПЗПИ-17-2');

INSERT INTO "public"."students" ("student_id", "group_id", "student_name", "student_additional_points") VALUES (1, 1, 'Иванов Иван Иванович', 0);
INSERT INTO "public"."students" ("student_id", "group_id", "student_name", "student_additional_points") VALUES (2, 1, 'Кириллов Кирилл Кириллович', 3.5);
INSERT INTO "public"."students" ("student_id", "group_id", "student_name", "student_additional_points") VALUES (3, 2, 'Петров Пётр Петрович', 7.8);

INSERT INTO "public"."subjects" ("subject_id", "subject_name") VALUES (1, 'Физика');
INSERT INTO "public"."subjects" ("subject_id", "subject_name") VALUES (2, 'Высшая математика');
INSERT INTO "public"."subjects" ("subject_id", "subject_name") VALUES (3, 'Основы права');

INSERT INTO "public"."learning" ("learning_id", "group_id", "subject_id", "learning_coefficient") VALUES (1, 1, 1, 5);
INSERT INTO "public"."learning" ("learning_id", "group_id", "subject_id", "learning_coefficient") VALUES (2, 2, 1, 5);
INSERT INTO "public"."learning" ("learning_id", "group_id", "subject_id", "learning_coefficient") VALUES (3, 1, 2, 6);
INSERT INTO "public"."learning" ("learning_id", "group_id", "subject_id", "learning_coefficient") VALUES (4, 2, 2, 6);

INSERT INTO "public"."progress" ("progress_id", "student_id", "learning_id", "progress_points") VALUES (1, 1, 1, 95);
INSERT INTO "public"."progress" ("progress_id", "student_id", "learning_id", "progress_points") VALUES (2, 1, 3, 91);
INSERT INTO "public"."progress" ("progress_id", "student_id", "learning_id", "progress_points") VALUES (3, 2, 1, 84);
INSERT INTO "public"."progress" ("progress_id", "student_id", "learning_id", "progress_points") VALUES (4, 2, 3, 71);
INSERT INTO "public"."progress" ("progress_id", "student_id", "learning_id", "progress_points") VALUES (5, 3, 2, 60);
INSERT INTO "public"."progress" ("progress_id", "student_id", "learning_id", "progress_points") VALUES (6, 3, 4, 65);