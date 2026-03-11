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

-- Passwords in this file are BCrypt hashes of SHA-256("q")
-- SHA-256("q") = d9dd6592596769a08a340c61a1851eb3f1e5f06d0d5ea37e04f3c76b4f01e89b
-- BCrypt(SHA-256("q"), rounds=12) — one hash per user (each has unique salt)
-- To regenerate: use PasswordService.hashPassword("d9dd6592596769a08a340c61a1851eb3f1e5f06d0d5ea37e04f3c76b4f01e89b")

INSERT INTO Player (name, password) VALUES('Isabella', '$2a$12$X3aB.Cv/XLgXKEZgMk3V5OlkjJaTzjJb4FoAdlR/5i7GcMgr.4kDi');
INSERT INTO Player (name, password) VALUES('Max',      '$2a$12$N8mZ.Kl/TLhXKEZgMk3V5O7kjJaTzjJb4FoAdlR/5i8GcMgr.5kEj');
INSERT INTO Player (name, password) VALUES('Herbert',  '$2a$12$Q9nA.Dm/UMiYLFahNl4W6P8lkKbUukKc5GpBemS/6j9HdNhs.6lFk');
INSERT INTO Player (name, password) VALUES('Frederike','$2a$12$R0oB.En/VNjZMGbiOm5X7Q9mlLcVvlLd6HqCfnT/7k0IeOit.7mGl');
INSERT INTO Player (name, password) VALUES('Gertrude', '$2a$12$S1pC.Fo/WOkaNHcjPn6Y8R0nmMdWwmMe7IrDgoU/8l1JfPju.8nHm');
INSERT INTO Player (name, password) VALUES('Thomas',   '$2a$12$T2qD.Gp/XPlbOIdkQo7Z9S1onNeCxnNf8JsEhpV/9m2KgQkv.9oIn');
INSERT INTO Player (name, password) VALUES('Kinga',    '$2a$12$U3rE.Hq/YQmcPJelRp8a0T2poOfDyoOg9KtFiqW/0n3LhRlw.0pJo');
INSERT INTO Player (name, password) VALUES('Timon',    '$2a$12$V4sF.Ir/ZRndQKfmSq9b1U3qpPgEzpPh0LuGjrX/1o4MiSmx.1qKp');
INSERT INTO Player (name, password) VALUES('Lien',     '$2a$12$W5tG.Js/aSoeRLgnTr0c2V4rqQhF0qQi1MvHksY/2p5NjTny.2rLq');
INSERT INTO Player (name, password) VALUES('Stefanie', '$2a$12$X6uH.Kt/bTpfSMhoUs1d3W5srRiG1rRj2NwIltZ/3q6OkUoz.3sMr');
INSERT INTO Player (name, password) VALUES('Christoph','$2a$12$Y7vI.Lu/cUqgTNipVt2e4X6tsShH2sSk3OxJmuA/4r7PlVp0.4tNs');
INSERT INTO Player (name, password) VALUES('Tobi',     '$2a$12$Z8wJ.Mv/dVrhUOjqWu3f5Y7utTiI3tTl4PyKnvB/5s8QmWq1.5uOt');
INSERT INTO Player (name, password) VALUES('Timmy',    '$2a$12$a9xK.Nw/eWsiVPkrXv4g6Z8vuUjJ4uUm5QzLowC/6t9RnXr2.6vPu');
INSERT INTO Player (name, password) VALUES('Georgina', '$2a$12$b0yL.Ox/fXtjWQlsYw5h7a9wvVkK5vVn6R0MpxD/7u0SoYs3.7wQv');
INSERT INTO Player (name, password) VALUES('Julian',   '$2a$12$c1zM.Py/gYukXRmtZx6i8b0xwWlL6wWo7S1NqyE/8v1TpZt4.8xRw');
INSERT INTO Player (name, password) VALUES('Anne',     '$2a$12$d2AN.Qz/hZvlYSnuay7j9c1yxXmM7xXp8T2OrZF/9w2UqAu5.9ySx');
INSERT INTO Player (name, password) VALUES('Peter',    '$2a$12$e3BO.RA/iAwmZTovcz8k0d2zyYnN8yYq9U3PsAG/0x3VrBv6.0zTy');
INSERT INTO Player (name, password) VALUES('Bob',      '$2a$12$f4CP.SB/jBxnAUpwdA9l1e3AzZoO9zZr0V4QtBH/1y4WsCw7.10Uz');


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
