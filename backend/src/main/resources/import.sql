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
INSERT INTO GroupEntity (name, id) VALUES ('Familie', 1);
INSERT INTO GroupEntity (name, id) VALUES ('4AHITM', 2);

-- Group 1 (Familie)
INSERT INTO Player (name, group_id, id) VALUES('Maria', 1, 1);
INSERT INTO Player (name, group_id, id) VALUES('Max', 1, 2);
INSERT INTO Player (name, group_id, id) VALUES('Herbert', 1, 3);
INSERT INTO Player (name, group_id, id) VALUES('Frederike', 1, 4);
INSERT INTO Player (name, group_id, id) VALUES('Gertrude', 1, 5);

-- Group 2 (4AHITM)
INSERT INTO Player (name, group_id, id) VALUES('Isabella', 2, 6);
INSERT INTO Player (name, group_id, id) VALUES('Thomas', 2, 7);
INSERT INTO Player (name, group_id, id) VALUES('Kinga', 2, 8);
INSERT INTO Player (name, group_id, id) VALUES('Timon', 2, 9);
INSERT INTO Player (name, group_id, id) VALUES('Lien', 2, 10);
INSERT INTO Player (name, group_id, id) VALUES('Stefanie', 2, 11);
INSERT INTO Player (name, group_id, id) VALUES('Christof', 2, 12);
INSERT INTO Player (name, group_id, id) VALUES('Tobi', 2, 13);


-- Insert a GroupQuestion using today's date
INSERT INTO GroupQuestion (question_id, group_id, date, id)
VALUES (7, 1, CURRENT_DATE, 1);

INSERT INTO GroupQuestionAnswer (answering_player_id, answer, group_question_id, id)
VALUES
    (1, 2, 1, 1),
    (2, 4, 1, 2),
    (3, 5, 1, 3),
    (4, 2, 1, 4),
    (5, 4, 1, 5);
