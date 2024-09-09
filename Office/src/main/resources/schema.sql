CREATE TABLE IF NOT EXISTS office
(
    id integer primary key generated always as identity,
    first_name VARCHAR(255),
    last_name VARCHAR
(
    255
),

    email VARCHAR
(
    255
),
    password VARCHAR(255),


    role VARCHAR(40)

    );