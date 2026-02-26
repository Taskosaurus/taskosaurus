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

Create a docker-compose.yaml file that contains a postgresql database. Make a file ".env" that contains the credemntials.
Username: taskosaurus
Password: taskosaurus
Database name: taskosaurus

---
  1. Kiss Marry Kill
  Is "Kiss Marry Kill" also in scope for this project, or only "Wer würde eher?"? The backend data model has no trace of it yet.

  ---
  2. Daily question — who triggers it?
  Is the daily question assigned automatically (e.g., server picks a random question at midnight) or manually by someone? And is
  it strictly one question per group per day?

  ---
  3. Voting mechanics
  - Can a player vote for themselves?
  - Can a player change their vote before the 24h window closes?
  - Is it one vote per player per question, or multiple?

  ---
  4. Joining a group
  From the git history I see QR code scanning. Is the flow:
  - Someone creates a group → backend generates the link → shown as QR code
  - Others scan the QR code → join the group
  Is that correct? And does creating a group require a "creator" / admin role?

  ---
  5. Authentication
  How is a player identified? Is there a login (username/password, Apple Sign-In, etc.), or is the player just identified by
  their playerId stored locally on the device?
