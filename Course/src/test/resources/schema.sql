DROP TABLE IF EXISTS course;

CREATE TABLE IF NOT EXISTS course
(
    id integer primary key generated always as identity,
    course_name VARCHAR(255),
    subject_name text[]);

INSERT INTO course (course_name, subject_name)VALUES ('bio-chem','{temat6, temat2, temat3}');

