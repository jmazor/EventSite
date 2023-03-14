drop database vudb;

CREATE DATABASE vudb;

USE vudb;

-- --------------------------------------------------------
create table super_admin
(
    id        CHAR(36) PRIMARY KEY DEFAULT UUID(),
    verification CHAR(36) NOT NULL DEFAULT UUID(),
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    email     VARCHAR(255) NOT NULL UNIQUE,
    password  VARCHAR(255) NOT NULL
);

-- --------------------------------------------------------

CREATE TABLE university
(
    id          CHAR(36) PRIMARY KEY DEFAULT UUID(),
    name        VARCHAR(255) NOT NULL,
    location    VARCHAR(255) NOT NULL,
    description TEXT,
    num_students INT          NOT NULL,
    picture     VARCHAR(255),
    admin_id     CHAR(36)     NOT NULL,
    emailDomain VARCHAR(255) NOT NULL

);

-- --------------------------------------------------------
CREATE TABLE student
(
    id        CHAR(36) PRIMARY KEY DEFAULT UUID(),
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    email     VARCHAR(255) NOT NULL UNIQUE,
    password  VARCHAR(255) NOT NULL,
    university_id CHAR(36) NOT NULL,
    FOREIGN KEY (university_id) REFERENCES university (id)
);


-- --------------------------------------------------------






-- --------------------------------------------------------
-- Create a super admin
INSERT INTO super_admin (id, first_name, last_name, email, password)
VALUES (UUID(), 'Super', 'Admin', 'superadmin@ucf.edu', 'password');


INSERT INTO university (id, name, location, description, num_students, picture, admin_id, emailDomain)
VALUES (UUID(), 'University of Central Florida', 'Orlando, FL', 'Public research university', 0, NULL,
        (SELECT id FROM super_admin), 'ucf.edu');

INSERT INTO student (id, first_name, last_name, email, password, university_id)
VALUES (UUID(), 'John', 'Doe', 'johndoe@ucf.edu', 'password', (SELECT id FROM university WHERE name = 'University of Central Florida'));

INSERT INTO student (id, first_name, last_name, email, password, university_id)
VALUES (UUID(), 'Jane', 'Doe', 'janedoe@ucf.edu', 'password', (SELECT id FROM university WHERE name = 'University of Central Florida'));

INSERT INTO student (id, first_name, last_name, email, password, university_id)
VALUES (UUID(), 'Bob', 'Smith', 'bobsmith@ucf.edu', 'password', (SELECT id FROM university WHERE name = 'University of Central Florida'));

INSERT INTO student (id, first_name, last_name, email, password, university_id)
VALUES (UUID(), 'Sara', 'Lee', 'saralee@ucf.edu', 'password', (SELECT id FROM university WHERE name = 'University of Central Florida'));

INSERT INTO student (id, first_name, last_name, email, password, university_id)
VALUES (UUID(), 'Tom', 'Hanks', 'tomhanks@ucf.edu', 'password', (SELECT id FROM university WHERE name = 'University of Central Florida'));
