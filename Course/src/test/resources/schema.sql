DROP TABLE IF EXISTS course;

CREATE TABLE IF NOT EXISTS course
(
    id integer primary key generated always as identity,
    course_name VARCHAR(255),
    subject_name text[]);

INSERT INTO course (course_name, subject_name)VALUES ('course1','{subject1, subject2, subject3}');
INSERT INTO course (course_name, subject_name)VALUES ('course2','{subject4, subject5, subject6}');

