CREATE TABLE IF NOT EXISTS grades
(  id integer primary key  generated always as   identity,
   email VARCHAR(255) ,
    subject VARCHAR(255),
    grades_description text[],
    teacher VARCHAR (255));