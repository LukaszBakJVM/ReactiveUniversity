CREATE TABLE IF NOT EXISTS student
   ( id integer primary key  generated always as   identity,
    first_name VARCHAR (255),
    last_name VARCHAR (255),
    email VARCHAR (255),
    course VARCHAR (255));



CREATE TABLE IF NOT EXISTS grades
(  id integer primary key  generated always as   identity,
    email VARCHAR(255) ,
    subject VARCHAR(255),
    grades VARCHAR(255),
   time_stamp DATE,
    teacher VARCHAR (255));


