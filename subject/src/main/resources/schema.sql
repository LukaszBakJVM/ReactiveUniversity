CREATE TABLE IF NOT EXISTS subject
(
    id      integer primary key generated always as identity,
   -- userid  INT,
    subject VARCHAR(255)
   -- fork    boolean,
 --   name    VARCHAR(255),
 --   login   VARCHAR(255),
 --   branch  text[],
 --   sha     VARCHAR(255)
    );