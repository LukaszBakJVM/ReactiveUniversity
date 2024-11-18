CREATE TABLE IF NOT EXISTS registration
(
    id
    integer
    primary
    key
    generated
    always as
    identity,
    first_name
    VARCHAR
(
    255
),
    last_name VARCHAR
(
    255
),
    email VARCHAR
(
    255
),
    password VARCHAR
(
    255
),
    role VARCHAR
(
    255
),
    created_by VARCHAR
(
    255
),
    time_stamp TIMESTAMP WITH TIME ZONE,
                             last_modified_by VARCHAR (255),
    last_modified_date TIMESTAMP
                         WITH TIME ZONE);
