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
    id           CHAR(36) NOT NULL DEFAULT UUID() PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES user (id)
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
    email_domain VARCHAR(255) NOT NULL,
    FOREIGN KEY (admin_id) REFERENCES super_admin (id)
);

-- --------------------------------------------------------
CREATE TABLE user
(
    id           CHAR(36) NOT NULL PRIMARY KEY,
    university_id CHAR(36) NOT NULL,
    FOREIGN KEY (id) REFERENCES user (id),
    FOREIGN KEY (university_id) REFERENCES university (id)
);

-- Ensures that Student Email is part of Uni domain
-- TODO: verify that this works

DELIMITER //
CREATE TRIGGER validate_student_email
    BEFORE INSERT
    ON user
    FOR EACH ROW
BEGIN
    DECLARE uni_domain VARCHAR(255);
    SELECT email_domain INTO uni_domain FROM university WHERE id = NEW.id;
    IF SUBSTRING((SELECT email FROM user WHERE id = NEW.id),
                 INSTR((SELECT email FROM user WHERE id = NEW.id), '@') + 1) != uni_domain THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Email domain does not match university domain.';
    END IF;
END;
//

DELIMITER ;

-- --------------------------------------------------------
-- TODO: When a new RSO is created automatically add them to users
-- TODO: When an RSO is approved automatically add them to admin maybe use an RsoAdmin table

CREATE TABLE rso
(
    id           CHAR(36) PRIMARY KEY  DEFAULT UUID(),
    admin_id     CHAR(36)              NOT NULL,
    name         VARCHAR(255) NOT NULL,
    university_id CHAR(36)     NOT NULL,
    approval     BOOL         NOT NULL DEFAULT FALSE,
    FOREIGN KEY (admin_id) REFERENCES user (id),
    FOREIGN KEY (university_id) REFERENCES university (id) ON DELETE CASCADE
);

CREATE TABLE rso_users
(
    student_id CHAR(36) NOT NULL,
    rso_id     CHAR(36) NOT NULL,
    FOREIGN KEY (student_id) REFERENCES user (id),
    FOREIGN KEY (rso_id) REFERENCES rso (id),
    PRIMARY KEY (student_id, rso_id)
);
-- TODO: We can count rso_users to get the number of members
CREATE TABLE admin
(
    id CHAR(36) NOT NULL,
    rso_id     CHAR(36) NOT NULL,
    FOREIGN KEY (id) REFERENCES user (id),
    FOREIGN KEY (rso_id) REFERENCES rso (id)
);

-- Automatically Add User to RSO when created
DELIMITER //

CREATE TRIGGER trg_rso_create_user
    AFTER INSERT
    ON rso
    FOR EACH ROW
BEGIN
    INSERT INTO rso_users (student_id, rso_id) VALUES (NEW.admin_id, NEW.id);
END //

-- A RSO user must belong to the same University

CREATE TRIGGER trg_rso_check_uni
    BEFORE INSERT
    ON rso_users
    FOR EACH ROW
BEGIN
    DECLARE uID CHAR(36);
    DECLARE rID CHAR(36);

    SELECT university_id INTO uID FROM student WHERE id = NEW.student_id;
    SELECT university_id INTO rID FROM rso WHERE id = NEW.rso_id;

    IF uID <> rID THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'User must belong to the same university as the RSO';
    END IF;
END //

DELIMITER ;


-- Approves an RSO when 4 users
/*
DELIMITER //
CREATE TRIGGER trg_rso_approval
    AFTER INSERT
    ON RsoUsers
    FOR EACH ROW
BEGIN
    DECLARE num_active_users INT;
    SELECT COUNT(*) INTO num_active_users FROM RsoUsers WHERE rsoID = NEW.rsoID;
    IF num_active_users >= 4 THEN
        UPDATE Rso SET approval = TRUE WHERE id = NEW.rsoID;
    END IF;
END //
DELIMITER ;
*/

-- TODO: Change this to a procedure

DELIMITER //

CREATE TRIGGER trg_rso_admin
    AFTER INSERT
    ON rso_users
    FOR EACH ROW
BEGIN
    DECLARE num_active_users INT;
    SELECT COUNT(*) INTO num_active_users FROM rso_users WHERE rso_id = NEW.rso_id;
    IF num_active_users >= 4 THEN
        INSERT INTO admin (id, rso_id) VALUES (NEW.student_id, NEW.rso_id);
    END IF;
END //

DELIMITER ;

-- --------------------------------------------------------

CREATE TABLE event
(
    id           CHAR(36) PRIMARY KEY DEFAULT UUID(),
    user_id       CHAR(36)                                                                                                                                                                                          NOT NULL,
    name         VARCHAR(255)                                                                                                                                                                                      NOT NULL,
    category     ENUM ('academic', 'arts', 'career', 'performance', 'entertainment', 'health', 'holiday', 'meeting', 'forum', 'recreation', 'service', 'social', 'speaker', 'sports', 'tour', 'other', 'workshop') NOT NULL,
    description  TEXT                                                                                                                                                                                              NOT NULL,
    start_date    DATETIME                                                                                                                                                                                          NOT NULL,
    end_date      DATETIME                                                                                                                                                                                          NOT NULL,
    location_name VARCHAR(255)                                                                                                                                                                                      NOT NULL,
    location_url  VARCHAR(255),
    phone        VARCHAR(255),
    email        VARCHAR(255),
    approval     BOOL                 DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES user (id)
);

-- TODO: Trigger RSO event ensure ID is RSO and approval is True
-- TODO: Public Event Require Approval
-- TODO: Do we want approval for every event? or just visibility within Public

CREATE TABLE public_event
(
    admin_id    CHAR(36) NOT NULL,
    event_id    CHAR(36) NOT NULL PRIMARY KEY,
    visibility BOOL     NOT NULL DEFAULT FALSE,
    FOREIGN KEY (admin_id) references super_admin (id)
);

-- TODO: Trigger to ensure userID is a SuperAdmin

CREATE TABLE university_event
(
    university_id CHAR(36) NOT NULL,
    event_id      CHAR(36) NOT NULL PRIMARY KEY,
    FOREIGN KEY (university_id) REFERENCES university (id),
    FOREIGN KEY (event_id) REFERENCES event (id)
);

CREATE TABLE rso_event
(
    rso_id CHAR(36) NOT NULL,
    event_id      CHAR(36) NOT NULL PRIMARY KEY,
    FOREIGN KEY (event_id) REFERENCES event (id),
    FOREIGN KEY (rso_id) REFERENCES rso (id)
);


CREATE TABLE event_users
(
    user_id  CHAR(36) NOT NULL,
    event_id CHAR(36) NOT NULL,
    rating  INT,
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (event_id) REFERENCES event (id),
    PRIMARY KEY (user_id, event_id)
);

/*

TODO: Change this to work with our new tables
DELIMITER $$


CREATE TRIGGER trg_event_users_check_uni
    BEFORE INSERT
    ON event_users
    FOR EACH ROW
BEGIN
    DECLARE event_visibility ENUM ('public', 'private', 'rso');
    DECLARE event_uni_id CHAR(36);
    DECLARE event_rso_id CHAR(36);

    SELECT visibility, university_id, rso_id
    INTO event_visibility, event_uni_id, event_rso_id
    FROM events
    WHERE event_id = NEW.event_id;

    IF event_visibility = 'private' AND
       event_uni_id <> (SELECT university_id FROM users WHERE user_id = NEW.user_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'User must belong to the same university as the private event';
    END IF;

    IF event_visibility = 'rso' AND event_rso_id <> (SELECT rso_id FROM rso_users WHERE user_id = NEW.user_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'User must belong to the same RSO as the RSO event';
    END IF;
END $$

-- DELIMITER ;
*/

-- --------------------------------------------------------


CREATE Table comments
(
    id      CHAR(36) PRIMARY KEY DEFAULT UUID(),
    text    TEXT     NOT NULL,
    user_id  CHAR(36) NOT NULL,
    event_id CHAR(36) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (event_id) REFERENCES event (id)
);

-- Ensures User was part of event

DELIMITER //

CREATE TRIGGER trg_comment_check_user
    BEFORE INSERT
    ON comments
    FOR EACH ROW
BEGIN
    DECLARE user_count INT;
    SELECT COUNT(*) INTO user_count FROM event_users WHERE user_id = NEW.user_id AND event_id = NEW.event_id;
    IF user_count = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'User must be part of the event to comment on it';
    END IF;
END //

DELIMITER ;

-- --------------------------------------------------------
--                                                       --
--              Create Data for Testing                  --
--                                                       --
-- --------------------------------------------------------
-- Create a super_admin user
INSERT INTO user (id, first_name, last_name, email, password)
VALUES (UUID(), 'Super', 'Admin', 'superadmin@ucf.edu', 'password');

INSERT INTO super_admin (verification, id)
VALUES (UUID(), (SELECT id FROM user WHERE email = 'superadmin@ucf.edu'));

-- Create the UCF university and associate it with the super_admin
INSERT INTO university (id, name, location, description, num_students, picture, admin_id, email_domain)
VALUES (UUID(), 'University of Central Florida', 'Orlando, Florida', 'A public research university with over 69,000 students', 69000, 'https://upload.wikimedia.org/wikipedia/commons/thumb/5/5a/University_of_Central_Florida_seal.svg/1200px-University_of_Central_Florida_seal.svg.png', (SELECT id FROM super_admin), 'ucf.edu');

-- Insert five initial students
INSERT INTO user (id, first_name, last_name, email, password)
VALUES (UUID(), 'John', 'Doe', 'johndoe@ucf.edu', 'password');

INSERT INTO user (id, university_id)
VALUES ((SELECT id FROM user WHERE email = 'johndoe@ucf.edu'), (SELECT id FROM university WHERE name = 'University of Central Florida'));

INSERT INTO user (id, first_name, last_name, email, password)
VALUES (UUID(), 'Jane', 'Doe', 'janedoe@ucf.edu', 'password');

INSERT INTO user (id, university_id)
VALUES ((SELECT id FROM user WHERE email = 'janedoe@ucf.edu'), (SELECT id FROM university WHERE name = 'University of Central Florida'));

INSERT INTO user (id, first_name, last_name, email, password)
VALUES (UUID(), 'Bob', 'Smith', 'bobsmith@ucf.edu', 'password');

INSERT INTO user (id, university_id)
VALUES ((SELECT id FROM user WHERE email = 'bobsmith@ucf.edu'), (SELECT id FROM university WHERE name = 'University of Central Florida'));

INSERT INTO user (id, first_name, last_name, email, password)
VALUES (UUID(), 'Alice', 'Johnson', 'alicejohnson@ucf.edu', 'password');

INSERT INTO user (id, university_id)
VALUES ((SELECT id FROM user WHERE email = 'alicejohnson@ucf.edu'), (SELECT id FROM university WHERE name = 'University of Central Florida'));

INSERT INTO user (id, first_name, last_name, email, password)
VALUES (UUID(), 'Tom', 'Wilson', 'tomwilson@ucf.edu', 'password');

INSERT INTO user (id, university_id)
VALUES ((SELECT id FROM user WHERE email = 'tomwilson@ucf.edu'), (SELECT id FROM university WHERE name = 'University of Central Florida'));

-- Start an RSO
INSERT INTO rso (id, admin_id, name, university_id, approval)
VALUES (UUID(), (SELECT id FROM user WHERE id = (SELECT id FROM user WHERE email = 'bobsmith@ucf.edu')), 'Chess Club', (SELECT id FROM university WHERE name = 'University of Central Florida'), 0);

-- Have the other two students join the RSO
INSERT INTO rso_users (student_id, rso_id)
VALUES ((SELECT id FROM user WHERE id = (SELECT id FROM user WHERE email = 'alicejohnson@ucf.edu')), (SELECT id FROM rso WHERE name = 'Chess Club'));

INSERT INTO rso_users (student_id, rso_id)
VALUES ((SELECT id FROM user WHERE id = (SELECT id FROM user WHERE email = 'tomwilson@ucf.edu')), (SELECT id FROM rso WHERE name = 'Chess Club'));

INSERT INTO rso_users (student_id, rso_id)
VALUES ((SELECT id FROM user WHERE id = (SELECT id FROM user WHERE email = 'janedoe@ucf.edu')), (SELECT id FROM rso WHERE name = 'Chess Club'));
-- Create an RSO event
INSERT INTO event (id, user_id, name, category, description, start_date, end_date, location_name, location_url, phone, email, approval)
VALUES (UUID(), (SELECT id FROM user WHERE id = (SELECT id FROM user WHERE email = 'bobsmith@ucf.edu')), 'Chess Tournament', 'recreation', 'Join us for a friendly chess tournament!', '2023-03-20 14:00:00', '2023-03-20 17:00:00', 'UCF Student Union', 'https://www.ucf.edu/about-ucf/map/', '555-555-5555', 'info@ucf.edu', 0);

-- Add the event to the RSO
INSERT INTO rso_event (rso_id, event_id)
VALUES ((SELECT id FROM rso WHERE name = 'Chess Club'), (SELECT id FROM event WHERE name = 'Chess Tournament'));


-- Add users to the RSO event
INSERT INTO event_users (user_id, event_id, rating)
VALUES ((SELECT id FROM user WHERE id = (SELECT id FROM user WHERE email = 'alicejohnson@ucf.edu')), (SELECT id FROM event WHERE name = 'Chess Tournament'), 5);

INSERT INTO event_users (user_id, event_id, rating)
VALUES ((SELECT id FROM user WHERE id = (SELECT id FROM user WHERE email = 'tomwilson@ucf.edu')), (SELECT id FROM event WHERE name = 'Chess Tournament'), 4);



