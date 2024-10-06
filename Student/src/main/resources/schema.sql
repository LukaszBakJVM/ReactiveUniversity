CREATE TABLE IF NOT EXISTS student
   ( id integer primary key  generated always as   identity,
    first_name VARCHAR (255),
    last_name VARCHAR (255),
    email VARCHAR (255),
    course VARCHAR (255));