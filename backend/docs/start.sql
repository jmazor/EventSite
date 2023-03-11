CREATE DATABASE vudb;

USE vudb;

CREATE TABLE universities
(
    university_id CHAR(36) PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    location      VARCHAR(255) NOT NULL,
    description   TEXT,
    num_students  INT          NOT NULL,
    picture       VARCHAR(255)
);

CREATE TABLE users
(
    user_id       CHAR(36) PRIMARY KEY,
    first_name    VARCHAR(255)                             NOT NULL,
    last_name     VARCHAR(255)                             NOT NULL,
    email         VARCHAR(255)                             NOT NULL UNIQUE,
    password      VARCHAR(255)                             NOT NULL,
    user_level    ENUM ('super_admin', 'admin', 'student') NOT NULL,
    university_id CHAR(36),
    FOREIGN KEY (university_id) REFERENCES universities (university_id)
);

CREATE TABLE rso
(
    rso_id        CHAR(36) PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    university_id CHAR(36)     NOT NULL,
    admin_id      CHAR(36)     NOT NULL,
    FOREIGN KEY (admin_id) REFERENCES users (user_id),
    FOREIGN KEY (university_id) REFERENCES universities (university_id)
);

CREATE TABLE rso_users
(
    user_id CHAR(36) NOT NULL,
    rso_id  CHAR(36) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (rso_id) REFERENCES rso (rso_id),
    PRIMARY KEY (user_id, rso_id)
);

CREATE TABLE events
(
    event_id      CHAR(36) PRIMARY KEY,
    name          VARCHAR(255)                      NOT NULL,
-- category ENUM ('academic', 'arts', 'career', 'performance', 'entertainment', 'health', 'holiday', 'meeting', 'forum', 'recreation', 'service', 'social', 'speaker', 'sports', 'tour', 'other', 'workshop') NOT NULL,
    description   TEXT                              NOT NULL,
    start_date    DATETIME                          NOT NULL,
    end_date      DATETIME                          NOT NULL,
    location_name VARCHAR(255)                      NOT NULL,
    location_url  VARCHAR(255),
    phone         VARCHAR(255),
    email         VARCHAR(255),
    event_type    ENUM ('public', 'private', 'rso') NOT NULL,
    rso_id        CHAR(36),
    university_id CHAR(36)                          NOT NULL,
    FOREIGN KEY (university_id) REFERENCES universities (university_id),
    FOREIGN KEY (rso_id) REFERENCES rso (rso_id)
);