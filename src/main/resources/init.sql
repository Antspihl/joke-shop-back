-- drop tables
DROP TABLE IF EXISTS jokes CASCADE;
DROP TABLE IF EXISTS ratings CASCADE;

-- create tables
CREATE TABLE jokes
(
    id        SERIAL PRIMARY KEY,
    setup     VARCHAR(255) NOT NULL,
    punchline VARCHAR(255) NOT NULL
);
CREATE TABLE ratings
(
    rating_id    SERIAL PRIMARY KEY,
    product_id   INT NOT NULL REFERENCES jokes (id),
    rating_value INT NOT NULL CHECK (rating_value >= 0 AND rating_value <= 5)
);
CREATE TABLE users
(
    id            SERIAL PRIMARY KEY,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name     VARCHAR(100),
    is_admin      BOOLEAN DEFAULT false
);

-- add test data to the tables
INSERT INTO jokes (setup, punchline)
VALUES ('Why did the chicken cross the road?', 'To get to the other side');
INSERT INTO jokes (setup, punchline)
VALUES ('What do you call a fish with no eyes?', 'A fsh');
INSERT INTO jokes (setup, punchline)
VALUES ('What do you call a deer with no eyes?', 'No eye deer(No idea)');

INSERT INTO ratings (product_id, rating_value)
VALUES (1, 4);
INSERT INTO ratings (product_id, rating_value)
VALUES (2, 3);
INSERT INTO ratings (product_id, rating_value)
VALUES (3, 5);

INSERT INTO users (username, email, password_hash, full_name, is_admin)
VALUES ('admin', 'admin@gmail.com', '$2a$abcdefgh', 'Admin', true);
INSERT INTO users (username, email, password_hash, full_name, is_admin)
VALUES ('user', 'ilmar@gmail.com', '$2a$123456', 'Ilmar', false);

