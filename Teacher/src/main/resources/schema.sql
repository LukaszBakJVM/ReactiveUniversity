CREATE TABLE IF NOT EXISTS teacher
(
    id integer primary key generated always as identity,
    first_name VARCHAR(255),
   last_name VARCHAR
(
    255
),
    age INT,
    email VARCHAR
(
    255
),
    password VARCHAR(255),

    subject_name text[]

    );