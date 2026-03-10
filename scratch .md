    I have a project an I need a specification. Try to understand the data model first. Read backend/src/main and extract  
    the model. Do not make any code. Ask questions until everything is clear. I will answer.
    
    
    Additional context:
    Swift Projekt
    Gesellschaftsspiele Digitalisieren:
    Wer würde eher?
        • Man tritt einer Freundschaftsgruppe bei
        • Die Gruppe bekommt täglich eine Wer würde eher Frage in der App innerhalb 24h beantwortbar (Auswahl zwischen den Gruppenmitgliedern)danach Auswertung 
        • Auswertung: Jeder sieht wer für wen abgestimmt hat
            ◦ Nice to have: Fragenspezifisch anonyme Auswertung
        • Nächster Tag: neue Frage …
    Wahrheit oder Pflicht?
    Version mit einem Handy:
        • Person gibt alle Namen der Mitspieler an
        • Random personen picker (Flaschendrehen)
        • Auswahl zwischen Wahrheit oder Pflicht
        • Random Frage (von JSON Server)
        • Fertig Flaschendrehen…
    Nice To Have Version:
        • Personen melden sich auf eigenem Handy an
        • Jeder gibt 3 Wahrheiten u. 3 Pflichten ein
        • Random personen picker (Flaschendrehen)
        • Person sucht aus zwischen Wahrheit oder Pflicht
        • Random Frage der zuvor eingegebenen Wahrheiten od. Pflichten erscheint
        • Aktuelle Person drückt weiter
        • Flaschendrehen ….
    
    There is additional info in sprint1/Taskosaurus.pdf
pofm

1. the database folder contains a docker-compose.yaml file. We want to have postgres instead of h2Make a file ".env" in the database folder that contains the credentials for postgres. We start it by "cd compose; docker compose up".

Username: taskosaurus
Password: taskosaurus
Database name: taskosaurus

2. create a database/setup.sql. I want to have a setup.sql file that contains the full DDL. Add drop table and other drop statements so that I can reseed the database. User insert.sql to insert seed data.
3. generate a database/drop-and-create.sh script that uses command line psql to run setup.sql and insert.sql

Aberger Mac: claude --resume 70ece345-a7d6-4063-8316-90dea8c319c4


yes, rename database folder to compose