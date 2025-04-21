-- Add Questions
INSERT INTO Question (question) VALUES ('Wer würde am ehesten versuchen in einer Fremdsprache zu kommunizieren, indem er einfach deutsche Wörter mit einem Akzent ausspricht?');
INSERT INTO Question (question) VALUES ('Wer von uns würde am wenigsten das Haus verlassen, wenn der Handyakku fast leer ist, und deshalb lieber zu spät kommen?');
INSERT INTO Question (question) VALUES ('Wer lebt am stärksten nach dem Motto "Man lebt nur einmal im Leben"?');
INSERT INTO Question (question) VALUES ('Wer würde eine gesamte Gruppenreise dokumentieren, aber die Fotos nie mit uns teilen?');
INSERT INTO Question (question) VALUES ('Wer überreagiert am schnellsten?');
INSERT INTO Question (question) VALUES ('Wer ist am schnellsten Teil von enuen Trends?');
INSERT INTO Question (question) VALUES ('Jeder in der Gruppe zeigt dir einen Song. Wer hat den unvorhersehbarsten Musikgeschmack?');
INSERT INTO Question (question) VALUES ('Wer würde es am kürzesten in einer dreckigen Wohnung aushalten? Putzen ist nicht erlaubt!');
INSERT INTO Question (question) VALUES ('Wer ist am schnellsten Teil von enuen Trends?');
INSERT INTO Question (question) VALUES ('Wer würde am ehesten kriminell werden, um einem Freund bei etwas zu helfen?');
INSERT INTO Question (question) VALUES ('Wer würde am ehesten versuchen, sich mit Wortwitz und Charme aus einem Strafzettel herauszureden?');
INSERT INTO Question (question) VALUES ('Wer könnte die beste TikTok-Tanzperformance hinlegen?');
INSERT INTO Question (question) VALUES ('Wen würde man öffentlich am wenigsten als "politisch korrekt" einstufen?');
INSERT INTO Question (question) VALUES ('Ganz alleine in einem leeren Raum: Wer würde am schnellsten die Nerven verlieren?');


-- test data
INSERT INTO GroupEntity (name) VALUES ('Familie');
INSERT INTO GroupEntity (name) VALUES ('4AHITM');
INSERT INTO GroupEntity (name) VALUES ('Die 5 Freunde');
INSERT INTO GroupEntity (name) VALUES ('Die 3 ???');

-- Group 1 (Familie)
INSERT INTO Player (name, group_id) VALUES('Isabella', 1);
INSERT INTO Player (name, group_id) VALUES('Max', 1);
INSERT INTO Player (name, group_id) VALUES('Herbert', 1);
INSERT INTO Player (name, group_id) VALUES('Frederike', 1);
INSERT INTO Player (name, group_id) VALUES('Gertrude', 1);

-- Group 2 (4AHITM)
INSERT INTO Player (name, group_id) VALUES('Isabella', 2);
INSERT INTO Player (name, group_id) VALUES('Thomas', 2);
INSERT INTO Player (name, group_id) VALUES('Kinga', 2);
INSERT INTO Player (name, group_id) VALUES('Timon', 2);
INSERT INTO Player (name, group_id) VALUES('Lien', 2);
INSERT INTO Player (name, group_id) VALUES('Stefanie', 2);
INSERT INTO Player (name, group_id) VALUES('Christof', 2);
INSERT INTO Player (name, group_id) VALUES('Tobi', 2);

-- Group 3 (5 Freunde)
INSERT INTO Player (name, group_id) VALUES('Isabella', 3);
INSERT INTO Player (name, group_id) VALUES('Timmy', 3);
INSERT INTO Player (name, group_id) VALUES('Georgina', 3);
INSERT INTO Player (name, group_id) VALUES('Julian', 3);
INSERT INTO Player (name, group_id) VALUES('Anne', 3);

-- Group 4 (3 ???)
INSERT INTO Player (name, group_id) VALUES('Isabella', 4);
INSERT INTO Player (name, group_id) VALUES('Peter', 4);
INSERT INTO Player (name, group_id) VALUES('Bob', 4);

-- Insert a GroupQuestion using today's date
INSERT INTO GroupQuestion (question_id, group_id, date)
VALUES (7, 1, CURRENT_DATE);

INSERT INTO GroupQuestion (question_id, group_id, date)
VALUES (9, 2, CURRENT_DATE);

INSERT INTO GroupQuestionAnswer (answering_player_id, answer, group_question_id)
VALUES
    (1, 2, 1),
    (2, 2, 1),
    (3, 5, 1),
    (4, 2, 1),
    (5, 4, 1);

INSERT INTO GroupQuestionAnswer (answering_player_id, answer, group_question_id)
VALUES
    (11, 12, 2),
    (7, 11, 2),
    (8, 12, 2),
    (9, 6, 2),
    (10, 8, 2),
    (12, 12, 2),
    (13, 8, 2);

