-- drop tables
DROP TABLE IF EXISTS jokes CASCADE;
DROP TABLE IF EXISTS ratings CASCADE;

-- create tables
CREATE TABLE jokes (
                    id SERIAL PRIMARY KEY,
                    setup VARCHAR(255) NOT NULL,
                    punchline VARCHAR(255) NOT NULL
);
CREATE TABLE ratings (
                      id SERIAL PRIMARY KEY,
                      product_id INT NOT NULL REFERENCES jokes(id),
                      rating INT NOT NULL CHECK (rating >= 0 AND rating <= 5)
);

-- add jokes to the table
INSERT INTO jokes (setup, punchline) VALUES ('Why did the chicken cross the road?', 'To get to the other side');
INSERT INTO jokes (setup, punchline) VALUES ('What do you call a fish with no eyes?', 'A fsh');
INSERT INTO jokes (setup, punchline) VALUES ('What do you call a deer with no eyes?', 'No eye deer(No idea)');

-- add ratings to the table
INSERT INTO ratings (product_id, rating) VALUES (1, 4);
INSERT INTO ratings (product_id, rating) VALUES (2, 3);
INSERT INTO ratings (product_id, rating) VALUES (3, 5);
