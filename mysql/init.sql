CREATE DATABASE IF NOT EXISTS kgu;

USE kgu;

CREATE TABLE IF NOT EXISTS test (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    director VARCHAR(255) NOT NULL
);

INSERT INTO test (title, director) VALUES
('TEST 1', 'test 1'),
('TEST 2', 'test 2'),
('TEST 3', 'test 3');
