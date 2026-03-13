INSERT INTO Question (question) VALUES ('Wer würde am ehesten versuchen in einer Fremdsprache zu kommunizieren, indem er einfach deutsche Wörter mit einem Akzent ausspricht?');
INSERT INTO Question (question) VALUES ('Wer von uns würde am wenigsten das Haus verlassen, wenn der Handyakku fast leer ist, und deshalb lieber zu spät kommen?');
INSERT INTO Question (question) VALUES ('Wer lebt am stärksten nach dem Motto "Man lebt nur einmal im Leben"?');
INSERT INTO Question (question) VALUES ('Wer würde eine gesamte Gruppenreise dokumentieren, aber die Fotos nie mit uns teilen?');
INSERT INTO Question (question) VALUES ('Wer überreagiert am schnellsten?');
INSERT INTO Question (question) VALUES ('Jeder in der Gruppe zeigt dir einen Song. Wer hat den unvorhersehbarsten Musikgeschmack?');
INSERT INTO Question (question) VALUES ('Wer würde es am kürzesten in einer dreckigen Wohnung aushalten? Putzen ist nicht erlaubt!');
INSERT INTO Question (question) VALUES ('Wer ist am schnellsten Teil von neuen Trends?');
INSERT INTO Question (question) VALUES ('Wer würde am ehesten kriminell werden, um einem Freund bei etwas zu helfen?');
INSERT INTO Question (question) VALUES ('Wer würde am ehesten versuchen, sich mit Wortwitz und Charme aus einem Strafzettel herauszureden?');
INSERT INTO Question (question) VALUES ('Wer könnte die beste TikTok-Tanzperformance hinlegen?');
INSERT INTO Question (question) VALUES ('Wen würde man öffentlich am wenigsten als "politisch korrekt" einstufen?');
INSERT INTO Question (question) VALUES ('Ganz alleine in einem leeren Raum: Wer würde am schnellsten die Nerven verlieren?');

INSERT INTO GroupEntity (name, link) VALUES ('Familie', 'http://192.168.137.135:8080/api/group/join/1');
INSERT INTO GroupEntity (name, link) VALUES ('4AHITM', 'http://192.168.137.135:8080/api/group/join/2');
INSERT INTO GroupEntity (name, link) VALUES ('Die 5 Freunde', 'http://192.168.137.135:8080/api/group/join/3');
INSERT INTO GroupEntity (name, link) VALUES ('Die 3 ???', 'http://192.168.137.135:8080/api/group/join/4');


INSERT INTO Player (name, password) VALUES('Isabella', '$2a$12$R.O7pI9mDkF5hA6G7H8I9OeL1R2S3T4U5V6W7X8Y9Z0A1B2C3D4E5');
INSERT INTO Player (name, password) VALUES('Max',      '$2a$12$S.O8pJ9nElG6iB7H8I9J0OfM2S3T4U5V6W7X8Y9Z0A1B2C3D4E5F6');
INSERT INTO Player (name, password) VALUES('Herbert',  '$2a$12$T.P9qK0oFmH7jC8I9J0K1PgN3T4U5V6W7X8Y9Z0A1B2C3D4E5F6G7');
INSERT INTO Player (name, password) VALUES('Frederike','$2a$12$U.Q0rL1pGnI8kD9J0K1L2QhO4U5V6W7X8Y9Z0A1B2C3D4E5F6G7H');
INSERT INTO Player (name, password) VALUES('Gertrude', '$2a$12$V.R1sM2qHoJ9lE0K1L2M3RiP5V6W7X8Y9Z0A1B2C3D4E5F6G7H8');
INSERT INTO Player (name, password) VALUES('Thomas',   '$2a$12$W.S2tN3rIpK0mF1L2M3N4SjQ6W7X8Y9Z0A1B2C3D4E5F6G7H8I9');
INSERT INTO Player (name, password) VALUES('Kinga',    '$2a$12$X.T3uO4sJqL1nG2M3N4O5TkR7X8Y9Z0A1B2C3D4E5F6G7H8I9J0');
INSERT INTO Player (name, password) VALUES('Timon',    '$2a$12$Y.U4vP5tKrM2oH3N4O5P6UlS8Y9Z0A1B2C3D4E5F6G7H8I9J0K1');
INSERT INTO Player (name, password) VALUES('Lien',     '$2a$12$Z.V5wQ6uLsN3pI4O5P6Q7VmT9Z0A1B2C3D4E5F6G7H8I9J0K1L2');
INSERT INTO Player (name, password) VALUES('Stefanie', '$2a$12$a.W6xR7vMtO4qJ5P6Q7R8WnU0A1B2C3D4E5F6G7H8I9J0K1L2M3');
INSERT INTO Player (name, password) VALUES('Christoph','$2a$12$b.X7yS8wNuP5rK6Q7R8S9XoV1B2C3D4E5F6G7H8I9J0K1L2M3N4');
INSERT INTO Player (name, password) VALUES('Tobi',     '$2a$12$c.Y8zT9xOvQ6sL7R8S9T0YpW2C3D4E5F6G7H8I9J0K1L2M3N4O5');
INSERT INTO Player (name, password) VALUES('Timmy',    '$2a$12$d.Z9aU0yPwR7tM8S9T0U1ZqX3D4E5F6G7H8I9J0K1L2M3N4O5P6');
INSERT INTO Player (name, password) VALUES('Georgina', '$2a$12$e.A0bV1zQxS8uN9T0U1V2ArY4E5F6G7H8I9J0K1L2M3N4O5P6Q');
INSERT INTO Player (name, password) VALUES('Julian',   '$2a$12$f.B1cW2aRyT9vO0U1V2W3BsZ5F6G7H8I9J0K1L2M3N4O5P6Q7');
INSERT INTO Player (name, password) VALUES('Anne',     '$2a$12$g.C2dX3bSzU0wP1V2W3X4CtA6G7H8I9J0K1L2M3N4O5P6Q7R8');
INSERT INTO Player (name, password) VALUES('Peter',    '$2a$12$h.D3eY4cT0V1xQ2W3X4Y5DuB7H8I9J0K1L2M3N4O5P6Q7R8S9');
INSERT INTO Player (name, password) VALUES('Bob',      '$2a$12$i.E4fZ5dU1W2yR3X4Y5Z6EvC8I9J0K1L2M3N4O5P6Q7R8S9T0');

INSERT INTO Player_Group (player_id, group_id) VALUES (1, 1);
INSERT INTO Player_Group (player_id, group_id) VALUES (2, 1);
INSERT INTO Player_Group (player_id, group_id) VALUES (3, 1);
INSERT INTO Player_Group (player_id, group_id) VALUES (4, 1);
INSERT INTO Player_Group (player_id, group_id) VALUES (5, 1);
INSERT INTO Player_Group (player_id, group_id) VALUES (1, 2);
INSERT INTO Player_Group (player_id, group_id) VALUES (6, 2);
INSERT INTO Player_Group (player_id, group_id) VALUES (7, 2);
INSERT INTO Player_Group (player_id, group_id) VALUES (8, 2);
INSERT INTO Player_Group (player_id, group_id) VALUES (9, 2);
INSERT INTO Player_Group (player_id, group_id) VALUES (10, 2);
INSERT INTO Player_Group (player_id, group_id) VALUES (11, 2);
INSERT INTO Player_Group (player_id, group_id) VALUES (12, 2);
INSERT INTO Player_Group (player_id, group_id) VALUES (1, 3);
INSERT INTO Player_Group (player_id, group_id) VALUES (13, 3);
INSERT INTO Player_Group (player_id, group_id) VALUES (14, 3);
INSERT INTO Player_Group (player_id, group_id) VALUES (15, 3);
INSERT INTO Player_Group (player_id, group_id) VALUES (16, 3);
INSERT INTO Player_Group (player_id, group_id) VALUES (1, 4);
INSERT INTO Player_Group (player_id, group_id) VALUES (17, 4);
INSERT INTO Player_Group (player_id, group_id) VALUES (18, 4);

INSERT INTO GroupQuestion (question_id, group_id, date) VALUES (7, 1, CURRENT_DATE);
INSERT INTO GroupQuestion (question_id, group_id, date) VALUES (9, 2, CURRENT_DATE);

--INSERT INTO GroupQuestionAnswer (answering_player_id, answer, group_question_id) VALUES (1, 2, 1);
--INSERT INTO GroupQuestionAnswer (answering_player_id, answer, group_question_id) VALUES (2, 2, 1);
INSERT INTO GroupQuestionAnswer (answering_player_id, answer, group_question_id) VALUES (3, 5, 1);
INSERT INTO GroupQuestionAnswer (answering_player_id, answer, group_question_id) VALUES (4, 2, 1);
INSERT INTO GroupQuestionAnswer (answering_player_id, answer, group_question_id) VALUES (5, 4, 1);

INSERT INTO GroupQuestionAnswer (answering_player_id, answer, group_question_id) VALUES (11, 12, 2);
INSERT INTO GroupQuestionAnswer (answering_player_id, answer, group_question_id) VALUES (7, 11, 2);
INSERT INTO GroupQuestionAnswer (answering_player_id, answer, group_question_id) VALUES (8, 12, 2);
INSERT INTO GroupQuestionAnswer (answering_player_id, answer, group_question_id) VALUES (9, 6, 2);
INSERT INTO GroupQuestionAnswer (answering_player_id, answer, group_question_id) VALUES (10, 8, 2);
INSERT INTO GroupQuestionAnswer (answering_player_id, answer, group_question_id) VALUES (12, 12, 2);
INSERT INTO GroupQuestionAnswer (answering_player_id, answer, group_question_id) VALUES (6, 8, 2);


UPDATE Question SET shortened_question = 'In Fremdsprache mit Akzent sprechen' WHERE question LIKE '%Fremdsprache zu kommunizieren%';
UPDATE Question SET shortened_question = 'Zu spät wegen leerem Handy' WHERE question LIKE '%Handyakku fast leer%';
UPDATE Question SET shortened_question = 'YOLO leben' WHERE question LIKE '%Man lebt nur einmal%';
UPDATE Question SET shortened_question = 'Fotos nicht teilen' WHERE question LIKE '%Gruppenreise dokumentieren%';
UPDATE Question SET shortened_question = 'Schnell überreagieren' WHERE question LIKE '%überreagiert%';
UPDATE Question SET shortened_question = 'Unvorhersehbarer Musikgeschmack' WHERE question LIKE '%unvorhersehbarsten Musikgeschmack%';
UPDATE Question SET shortened_question = 'Dreckige Wohnung aushalten' WHERE question LIKE '%dreckigen Wohnung%';
UPDATE Question SET shortened_question = 'Neue Trends mitmachen' WHERE question LIKE '%Teil von neuen Trends%';
UPDATE Question SET shortened_question = 'Für Freund kriminell werden' WHERE question LIKE '%kriminell werden%';
UPDATE Question SET shortened_question = 'Aus Strafzettel herausreden' WHERE question LIKE '%Strafzettel%';
UPDATE Question SET shortened_question = 'TikTok-Tanz performen' WHERE question LIKE '%TikTok-Tanzperformance%';
UPDATE Question SET shortened_question = 'Nicht politisch korrekt' WHERE question LIKE '%politisch korrekt%';
UPDATE Question SET shortened_question = 'Alleine Nerven verlieren' WHERE question LIKE '%leeren Raum%';
