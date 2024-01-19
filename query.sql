USE `ou-social-network`;

INSERT INTO role(name) VALUES("ROLE_FORMER_STUDENT");
INSERT INTO role(name) VALUES("ROLE_TEACHER");
INSERT INTO role(name) VALUES("ROLE_ADMIN");

INSERT INTO account(email, password, status, created_date, role_id)
VALUES ('admin456@gmail.com',
'$2a$10$3YohAzphxM8cU1uvEkikleeAf4xPlPZrQ0eqx5iPgc0bcUT48j/SC', 'ACTIVE', now(), '3');

INSERT INTO user(id, first_name, last_name, dob, avatar, cover_avatar)
VALUES (1, 'TP.HCM', 'Trường Đại học Mở', '1993-07-26',
'https://ou.edu.vn/wp-content/uploads/2019/01/OpenUniversity.png',
'https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907605/emgfalpnxzxyclg2eusk.png');

INSERT INTO reaction(name) VALUES ('Thích');
INSERT INTO reaction(name) VALUES ('Haha');
INSERT INTO reaction(name) VALUES ('Tim');

INSERT INTO question_type(type) VALUES ('Multiple choice question');
INSERT INTO question_type(type) VALUES ('Input text question');
INSERT INTO question_type(type) VALUES ('Checkbox question');

SET SQL_SAFE_UPDATES = 0;
UPDATE `ou-social-network`.comment
SET level = 1