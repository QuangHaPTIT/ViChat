CREATE TABLE IF NOT EXISTS medias (
    id INT PRIMARY KEY AUTO_INCREMENT,
    url VARCHAR(255) NOT NULL ,
    media_type VARCHAR(100),
    message_id INT,
    FOREIGN KEY(message_id) REFERENCES messages(id)
);
