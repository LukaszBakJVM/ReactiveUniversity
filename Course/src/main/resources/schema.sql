CREATE TABLE IF NOT EXISTS course
(
    id integer primary key generated always as identity,
    course_name VARCHAR(255),

    subject_name text[]);