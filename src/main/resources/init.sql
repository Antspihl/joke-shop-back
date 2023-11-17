-- drop tables
DROP TABLE IF EXISTS jokes CASCADE;
DROP TABLE IF EXISTS ratings CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS shop_carts CASCADE;
DROP TABLE IF EXISTS cart_items CASCADE;

-- create tables
CREATE TABLE jokes
(
    joke_id SERIAL PRIMARY KEY,
    setup VARCHAR(255) NOT NULL,
    punchline VARCHAR(255) NOT NULL,
    price NUMERIC(8,2) DEFAULT 1,
    times_bought INT DEFAULT 0
);
CREATE TABLE users
(
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    is_admin BOOLEAN DEFAULT false
);
CREATE TABLE ratings
(
    rating_id SERIAL PRIMARY KEY,
    rating_value INT NOT NULL CHECK (rating_value >= 0 AND rating_value <= 5),
    joke_id INT NOT NULL REFERENCES jokes (joke_id) ON DELETE CASCADE,
    author_id INT NOT NULL REFERENCES users (user_id) ON DELETE CASCADE
);
CREATE TABLE shop_carts
(
    cart_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE cart_items
(
    item_id SERIAL PRIMARY KEY,
    cart_id INT NOT NULL,
    joke_id INT NOT NULL,
    FOREIGN KEY (cart_id) REFERENCES shop_carts (cart_id),
    FOREIGN KEY (joke_id) REFERENCES jokes (joke_id)
);

-- add test data to the tables
INSERT INTO jokes (setup, punchline, price)
VALUES ('Why did the chicken cross the road?', 'To get to the other side', 10.00);
INSERT INTO jokes (setup, punchline, price)
VALUES ('What do you call a fish with no eyes?', 'A fsh', 15.00);
INSERT INTO jokes (setup, punchline)
VALUES ('What do you call a deer with no eyes?', 'No eye deer(No idea)');

INSERT INTO users (username, email, password_hash, full_name, is_admin)
VALUES ('admin', 'admin@gmail.com', '$2a$abcdefgh', 'Admin', true);
INSERT INTO users (username, email, password_hash, full_name, is_admin)
VALUES ('user', 'ilmar@gmail.com', '$2a$123456', 'Ilmar', false);
INSERT INTO users (username, email, password_hash, full_name, is_admin)
VALUES ('test1', 'test1@gmail.com', '$2a$1234567', 'Ants', true);
INSERT INTO users (username, email, password_hash, full_name, is_admin)
VALUES ('test2', 'test2@gmail.com', '$2a$1234568', 'Pets', true);
INSERT INTO users (username, email, password_hash, full_name, is_admin)
VALUES ('test3', 'test3@gmail.com', '$2a$1234569', 'Pets', true);
INSERT INTO users (username, email, password_hash, full_name, is_admin)
VALUES ('test4', 'test4@gmail.com', '$2a$1234560', 'test', false);
INSERT INTO users (username, email, password_hash, full_name, is_admin)
VALUES ('test5', 'test5@gmail.com', '$2a$1234577', 'Ants', true);
INSERT INTO users (username, email, password_hash, full_name, is_admin)
VALUES ('test6', 'test6@gmail.com', '$2a$1234578', 'Pets', true);
INSERT INTO users (username, email, password_hash, full_name, is_admin)
VALUES ('test7', 'test7@gmail.com', '$2a$1234579', 'Pets', true);
INSERT INTO users (username, email, password_hash, full_name, is_admin)
VALUES ('test8', 'test8@gmail.com', '$2a$1234570', 'test', false);
INSERT INTO users (username, email, password_hash, full_name, is_admin)
VALUES ('test9', 'test9@gmail.com', '$2a$1234571', 'Peedu', false);

INSERT INTO ratings (joke_id, rating_value, author_id)
VALUES (1, 4, 2);
INSERT INTO ratings (joke_id, rating_value, author_id)
VALUES (2, 3, 2);
INSERT INTO ratings (joke_id, rating_value, author_id)
VALUES (3, 5, 2);

INSERT INTO shop_carts (user_id)
VALUES (2);

INSERT INTO cart_items (cart_id, joke_id)
VALUES (1, 1);
INSERT INTO cart_items (cart_id, joke_id)
VALUES (1, 2);

