DROP TABLE IF EXISTS user,room,message;

CREATE TABLE user
(
    id       INTEGER      NOT NULL AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE room
(
    id   INTEGER      NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE message
(
    id        INTEGER     NOT NULL AUTO_INCREMENT,
    owner     INTEGER     NOT NULL,
    room      INTEGER     NOT NULL,
    content   TEXT        NOT NULL,
    send_time DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (owner) REFERENCES user (id),
    FOREIGN KEY (room) REFERENCES room (id)
);