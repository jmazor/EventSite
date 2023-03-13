drop database vudb;

CREATE DATABASE vudb;

USE vudb;

-- --------------------------------------------------------
CREATE TABLE user
(
    id        CHAR(36) PRIMARY KEY DEFAULT UUID(),
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    email     VARCHAR(255) NOT NULL UNIQUE,
    password  VARCHAR(255) NOT NULL
);

CREATE TABLE super_admin
(
    verification CHAR(36) NOT NULL DEFAULT UUID(),
    id           CHAR(36) NOT NULL DEFAULT UUID() PRIMARY KEY ,
    FOREIGN KEY (id) REFERENCES user (id)
);

-- --------------------------------------------------------

-- Create a super admin
INSERT INTO user (id, first_name, last_name, email, password)
VALUES (UUID(), 'Super', 'Admin', 'superadmin@ucf.edu', 'password');

INSERT INTO super_admin (verification, id)
VALUES (UUID(), (SELECT id FROM user WHERE email = 'superadmin@ucf.edu'));