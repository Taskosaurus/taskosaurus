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


INSERT INTO Player (name, password) VALUES('Isabella', '$2a$12$0vV1vR9HjX6h6.q4T/R.Ue6rVnU6X1vI9S8L7M6K5J4I3H2G1F0E.');
INSERT INTO Player (name, password) VALUES('Max',      '$2a$12$K1G2H3I4J5K6L7M8N9O0P.u8u7v6w5x4y3z2A1B2C3D4E5F6G7H8I');
INSERT INTO Player (name, password) VALUES('Herbert',  '$2a$12$mP5R7S9T1U3V5W7X9Y1Z2.uYvXwWvUuTtSsRrQqPpOoNnMmLlKkJj');
INSERT INTO Player (name, password) VALUES('Frederike','$2a$12$fL7mN8o9p0q1r2s3t4u5v.uY7x6w5v4u3t2s1r0q9p8o7n6m5l4k');
INSERT INTO Player (name, password) VALUES('Gertrude', '$2a$12$wR3tV5zX8b7p1f4h7j0m4.6O9l8k7j6i5h4g3f2e1d0c9b8a7y6x');
INSERT INTO Player (name, password) VALUES('Thomas',   '$2a$12$G1J2K3L4M5N6O7P8Q9R0S.uS8r7q6p5o4n3m2l1k0j9i8h7g6f5e');
INSERT INTO Player (name, password) VALUES('Kinga',    '$2a$12$T2uV3wX4y5z6A7B8C9D0E.uT5s4r3q2p1o0n9m8l7k6j5i4h3g2f');
INSERT INTO Player (name, password) VALUES('Timon',    '$2a$12$M1N2O3P4Q5R6S7T8U9V0W.uR2q1p0o9n8m7l6k5j4i3h2g1f0e9d');
INSERT INTO Player (name, password) VALUES('Lien',     '$2a$12$B1C2D3E4F5G6H7I8J9K0L.uL9k8j7i6h5g4f3e2d1c0b9a8z7y6');
INSERT INTO Player (name, password) VALUES('Stefanie', '$2a$12$X1Y2Z3A4B5C6D7E8F9G0H.uH6g5f4e3d2c1b0a9z8y7x6w5v4u3');
INSERT INTO Player (name, password) VALUES('Christoph','$2a$12$O1P2Q3R4S5T6U7V8W9X0Y.uY7x6w5v4u3t2s1r0q9p8o7n6m5l4k');
INSERT INTO Player (name, password) VALUES('Tobi',     '$2a$12$Z1A2B3C4D5E6F7G8H9I0J.uJ0i9h8g7f6e5d4c3b2a1z0y9x8w7v');
INSERT INTO Player (name, password) VALUES('Timmy',    '$2a$12$K1L2M3N4O5P6Q7R8S9T0U.uU1t0s9r8q7p6o5n4m3l2k1j0i9h8g');
INSERT INTO Player (name, password) VALUES('Georgina', '$2a$12$V1W2X3Y4Z5A6B7C8D9E0F.uF9e8d7c6b5a4z3y2x1w0v9u8t7s6');
INSERT INTO Player (name, password) VALUES('Julian',   '$2a$12$G1H2I3J4K5L6M7N8O9P0Q.uQ0p9o8n7m6l5k4j3i2h1g0f9e8d7c');
INSERT INTO Player (name, password) VALUES('Anne',     '$2a$12$R1S2T3U4V5W6X7Y8Z9A0B.uB0a9z8y7x6w5v4u3t2s1r0q9p8o7n');
INSERT INTO Player (name, password) VALUES('Peter',    '$2a$12$C1D2E3F4G5H6I7J8K9L0M.uM0l9k8j7i6h5g4f3e2d1c0b9a8z7y');
INSERT INTO Player (name, password) VALUES('Bob',      '$2a$12$N1O2P3Q4R5S6T7U8V9W0X.uX0w9v8u7t6s5r4q3p2o1n0m9l8k7j');


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
