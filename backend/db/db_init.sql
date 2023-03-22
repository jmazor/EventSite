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
CREATE INDEX idx_user_email ON user (email);

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
CREATE TABLE student
(
    id           CHAR(36) NOT NULL PRIMARY KEY,
    university_id CHAR(36) NOT NULL,
    FOREIGN KEY (id) REFERENCES user (id) ON DELETE CASCADE,
    FOREIGN KEY (university_id) REFERENCES university (id) ON DELETE CASCADE,
);

-- Ensures that Student Email is part of Uni domain
-- TODO: verify that this works

DELIMITER //
CREATE TRIGGER validate_student_email
    BEFORE INSERT
    ON student
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
    name         VARCHAR(255)                                                                                                                                                                                      NOT NULL,
    category     ENUM ('academic', 'arts', 'career', 'performance', 'entertainment', 'health', 'holiday', 'meeting', 'forum', 'recreation', 'service', 'social', 'speaker', 'sports', 'tour', 'other', 'workshop') NOT NULL,
    description  TEXT                                                                                                                                                                                              NOT NULL,
    start_date    DATETIME                                                                                                                                                                                          NOT NULL,
    end_date      DATETIME                                                                                                                                                                                          NOT NULL,
    location_name VARCHAR(255)                                                                                                                                                                                      NOT NULL,
    location_url  VARCHAR(255),
    phone        VARCHAR(255),
    email        VARCHAR(255)
);

-- TODO: Trigger RSO event ensure ID is RSO and approval is True
-- TODO: Public Event Require Approval
-- TODO: Do we want approval for every event? or just visibility within Public

CREATE TABLE public_event
(
    admin_id    CHAR(36) NOT NULL,
    id    CHAR(36) NOT NULL PRIMARY KEY,
    approval     BOOL                 DEFAULT FALSE,
    FOREIGN KEY (admin_id) references super_admin (id),
    FOREIGN KEY (id) REFERENCES event (id)
);

-- TODO: Trigger to ensure userID is a SuperAdmin

CREATE TABLE private_event
(
    university_id CHAR(36) NOT NULL,
    id      CHAR(36) NOT NULL PRIMARY KEY,
    FOREIGN KEY (university_id) REFERENCES university (id),
    FOREIGN KEY (id) REFERENCES event (id)
);

CREATE TABLE rso_event
(
    rso_id CHAR(36) NOT NULL,
    id      CHAR(36) NOT NULL PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES event (id),
    FOREIGN KEY (rso_id) REFERENCES rso (id)
);


CREATE TABLE event_users
(
    user_id  CHAR(36) NOT NULL,
    id CHAR(36) NOT NULL,
    rating  INT,
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (id) REFERENCES event (id),
    PRIMARY KEY (user_id, id)
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

