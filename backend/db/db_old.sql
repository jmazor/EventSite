drop database vudb;

CREATE DATABASE vudb;

USE vudb;

-- --------------------------------------------------------
CREATE TABLE User
(
    id        CHAR(36) PRIMARY KEY DEFAULT UUID(),
    firstName VARCHAR(255) NOT NULL,
    lastName  VARCHAR(255) NOT NULL,
    email     VARCHAR(255) NOT NULL UNIQUE,
    password  VARCHAR(255) NOT NULL
);

CREATE TABLE SuperAdmin
(
    verification CHAR(36) NOT NULL DEFAULT UUID(),
    id           CHAR(36) NOT NULL DEFAULT UUID(),
    FOREIGN KEY (id) REFERENCES User (id)
);

-- --------------------------------------------------------

CREATE TABLE University
(
    id          CHAR(36) PRIMARY KEY DEFAULT UUID(),
    name        VARCHAR(255) NOT NULL,
    location    VARCHAR(255) NOT NULL,
    description TEXT,
    numStudents INT          NOT NULL,
    picture     VARCHAR(255),
    adminID     CHAR(36)     NOT NULL,
    emailDomain VARCHAR(255) NOT NULL,
    FOREIGN KEY (adminID) REFERENCES SuperAdmin (id)
);

-- --------------------------------------------------------

CREATE TABLE Student
(
    universityID CHAR(36),
    id           CHAR(36) NOT NULL,
    FOREIGN KEY (id) REFERENCES User (id),
    FOREIGN KEY (universityID) REFERENCES University (id)
);

-- Ensures that Student Email is part of Uni domain

DELIMITER //
CREATE TRIGGER validate_student_email
    BEFORE INSERT
    ON Student
    FOR EACH ROW
BEGIN
    DECLARE uni_domain VARCHAR(255);
    SELECT emailDomain INTO uni_domain FROM University WHERE id = NEW.universityID;
    IF SUBSTRING((SELECT email FROM User WHERE id = NEW.id),
                 INSTR((SELECT email FROM User WHERE id = NEW.id), '@') + 1) != uni_domain THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Email domain does not match university domain.';
    END IF;
END;
//
DELIMITER ;

-- --------------------------------------------------------
-- TODO: When a new RSO is created automatically add them to users
-- TODO: When an RSO is approved automatically add them to admin maybe use an RsoAdmin table

CREATE TABLE Rso
(
    id           CHAR(36) PRIMARY KEY  DEFAULT UUID(),
    adminID     CHAR(36)              NOT NULL,
    name         VARCHAR(255) NOT NULL,
    universityID CHAR(36)     NOT NULL,
    approval     BOOL         NOT NULL DEFAULT FALSE,
    FOREIGN KEY (adminID) REFERENCES Student (id),
    FOREIGN KEY (universityID) REFERENCES University (id) ON DELETE CASCADE
);

CREATE TABLE RsoUsers
(
    studentID CHAR(36) NOT NULL,
    rsoID     CHAR(36) NOT NULL,
    FOREIGN KEY (studentID) REFERENCES Student (id),
    FOREIGN KEY (rsoID) REFERENCES Rso (id),
    PRIMARY KEY (studentID, rsoID)
);

CREATE TABLE Admin
(
    id CHAR(36) NOT NULL,
    rsoID     CHAR(36) NOT NULL,
    FOREIGN KEY (id) REFERENCES Student (id),
    FOREIGN KEY (rsoID) REFERENCES Rso (id)
);

-- Automatically Add User to RSO when created
DELIMITER //

CREATE TRIGGER trg_rso_create_user
    AFTER INSERT
    ON Rso
    FOR EACH ROW
BEGIN
    INSERT INTO RsoUsers (studentID, rsoID) VALUES (NEW.adminID, NEW.id);
END //


-- A RSO student must belong to the same University

CREATE TRIGGER trg_rso_check_uni
    BEFORE INSERT
    ON RsoUsers
    FOR EACH ROW
BEGIN
    DECLARE uID CHAR(36);
    DECLARE rID CHAR(36);

    SELECT universityID INTO uID FROM Student WHERE id = NEW.studentID;
    SELECT universityID INTO rID FROM Rso WHERE id = NEW.rsoID;

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
    ON RsoUsers
    FOR EACH ROW
BEGIN
    DECLARE num_active_users INT;
    SELECT COUNT(*) INTO num_active_users FROM RsoUsers WHERE rsoID = NEW.rsoID;
    IF num_active_users >= 4 THEN
        INSERT INTO Admin (id, rsoID) VALUES (NEW.studentID, NEW.rsoID);
    END IF;
END //

DELIMITER ;



-- --------------------------------------------------------


CREATE TABLE Event
(
    id           CHAR(36) PRIMARY KEY DEFAULT UUID(),
    userID       CHAR(36)                                                                                                                                                                                          NOT NULL,
    name         VARCHAR(255)                                                                                                                                                                                      NOT NULL,
    category     ENUM ('academic', 'arts', 'career', 'performance', 'entertainment', 'health', 'holiday', 'meeting', 'forum', 'recreation', 'service', 'social', 'speaker', 'sports', 'tour', 'other', 'workshop') NOT NULL,
    description  TEXT                                                                                                                                                                                              NOT NULL,
    startDate    DATETIME                                                                                                                                                                                          NOT NULL,
    endDate      DATETIME                                                                                                                                                                                          NOT NULL,
    locationName VARCHAR(255)                                                                                                                                                                                      NOT NULL,
    locationUrl  VARCHAR(255),
    phone        VARCHAR(255),
    email        VARCHAR(255),
    approval     BOOL                 DEFAULT FALSE,
    FOREIGN KEY (userID) REFERENCES User (id)
);

-- TODO: Trigger RSO event ensure ID is RSO and approval is True
-- TODO: Public Event Require Approval
-- TODO: Do we want approval for every event? or just visibility within Public

CREATE TABLE PublicEvent
(
    adminID    CHAR(36) NOT NULL,
    eventID    CHAR(36) NOT NULL,
    visibility BOOL     NOT NULL DEFAULT FALSE,
    FOREIGN KEY (adminID) references SuperAdmin (id)
);

-- TODO: Trigger to ensure userID is a SuperAdmin

CREATE TABLE UniversityEvent
(
    universityID CHAR(36) NOT NULL,
    FOREIGN KEY (universityID) REFERENCES University (id)
);

CREATE TABLE RsoEvent
(
    rsoID CHAR(36) NOT NULL,
    FOREIGN KEY (rsoID) REFERENCES Rso (id)
);


CREATE TABLE EventUsers
(
    userID  CHAR(36) NOT NULL,
    eventID CHAR(36) NOT NULL,
    rating  INT,
    FOREIGN KEY (userID) REFERENCES User (id),
    FOREIGN KEY (eventID) REFERENCES Event (id),
    PRIMARY KEY (userID, eventID)
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


CREATE Table Comments
(
    id      CHAR(36) PRIMARY KEY DEFAULT UUID(),
    text    TEXT     NOT NULL,
    userID  CHAR(36) NOT NULL,
    eventID CHAR(36) NOT NULL,
    FOREIGN KEY (userID) REFERENCES User (id),
    FOREIGN KEY (eventID) REFERENCES Event (id)
);

-- Ensures User was part of event

DELIMITER //

CREATE TRIGGER trg_comment_check_user
    BEFORE INSERT
    ON Comments
    FOR EACH ROW
BEGIN
    DECLARE user_count INT;
    SELECT COUNT(*) INTO user_count FROM EventUsers WHERE userID = NEW.userID AND eventID = NEW.eventID;
    IF user_count = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'User must be part of the event to comment on it';
    END IF;
END //

DELIMITER ;

-- --------------------------------------------------------

USE vudb;

-- Create a super admin
INSERT INTO User (id, firstName, lastName, email, password)
VALUES (UUID(), 'Super', 'Admin', 'superadmin@ucf.edu', 'password');

INSERT INTO SuperAdmin (verification, id)
VALUES (UUID(), (SELECT id FROM User WHERE email = 'superadmin@ucf.edu'));

-- Create a university
INSERT INTO University (id, name, location, description, numStudents, picture, adminID, emailDomain)
VALUES (UUID(), 'University of Central Florida', 'Orlando, FL', 'Public research university', 0, NULL,
        (SELECT id FROM SuperAdmin), 'ucf.edu');

-- Create two students in the university
INSERT INTO User (id, firstName, lastName, email, password)
VALUES (UUID(), 'John', 'Doe', 'johndoe@ucf.edu', 'password');
INSERT INTO Student (universityID, id)
VALUES ((SELECT id FROM University), (SELECT id FROM User WHERE email = 'johndoe@ucf.edu'));

INSERT INTO User (id, firstName, lastName, email, password)
VALUES (UUID(), 'Jane', 'Doe', 'janedoe@ucf.edu', 'password');
INSERT INTO Student (universityID, id)
VALUES ((SELECT id FROM University), (SELECT id FROM User WHERE email = 'janedoe@ucf.edu'));

INSERT INTO User (id, firstName, lastName, email, password)
VALUES (UUID(), 'Bob', 'Smith', 'bobsmith@ucf.edu', 'password');
INSERT INTO Student (universityID, id)
VALUES ((SELECT id FROM University), (SELECT id FROM User WHERE email = 'bobsmith@ucf.edu'));

INSERT INTO User (id, firstName, lastName, email, password)
VALUES (UUID(), 'Alice', 'Johnson', 'alicejohnson@ucf.edu', 'password');
INSERT INTO Student (universityID, id)
VALUES ((SELECT id FROM University), (SELECT id FROM User WHERE email = 'alicejohnson@ucf.edu'));

INSERT INTO User (id, firstName, lastName, email, password)
VALUES (UUID(), 'Tom', 'Wilson', 'tomwilson@ucf.edu', 'password');
INSERT INTO Student (universityID, id)
VALUES ((SELECT id FROM University), (SELECT id FROM User WHERE email = 'tomwilson@ucf.edu'));

-- Start an RSO
INSERT INTO Rso (id, adminID, name, universityID, approval)
VALUES (UUID(), (SELECT id FROM User WHERE email = 'bobsmith@ucf.edu'), 'Chess Club', (SELECT id FROM University), 0);

-- Have the other two students join the RSO
INSERT INTO RsoUsers (studentID, rsoID)
VALUES ((SELECT id FROM User WHERE email = 'alicejohnson@ucf.edu'), (SELECT id FROM Rso WHERE name = 'Chess Club'));

INSERT INTO RsoUsers (studentID, rsoID)
VALUES ((SELECT id FROM User WHERE email = 'tomwilson@ucf.edu'), (SELECT id FROM Rso WHERE name = 'Chess Club'));

-- SET @test = (SELECT id FROM Rso WHERE name = 'Chess Club');

INSERT INTO RsoUsers (studentID, rsoID)
VALUES ((SELECT id FROM User WHERE email = 'janedoe@ucf.edu'), (SELECT id FROM Rso WHERE name = 'Chess Club'));

-- --------------------------------