1. Es soll eine **iOS und eine Android** applikation geben

2. a) Nein der Backend api vertrag muss nicht versioniert werden
b) Nein es musss nicht offline fähig sein

3. Ja als Spieler soll man von verschiedenen Geräte auf die Gruppen zugreifen können

Phase 2
Ein Spieler hat einen Account, dieser User kann sich dann auf mehreren Geräten anmelden

2.2 Es soll als Authentifizierung einen Nickname+PIN geben

2.3 Ja der Spieler soll Gruppen verlassen können und wieder beitreten. ER soll auch seinen Namen ändern. Nein er soll nicht mehrere Accounts beseitzen

Phase 3
3.1 Es soll einen Owner geben, der die grupppe erstellt hat. dieser soll dann leute raus kicken können und sachen bearbeiten. aber jeder kann Gruppen erstellen

3.2 
Ja ein Owner darf mitglieder kicken, bannedn den gruppennamen ändern. Er darf aber keine Fragen überspringen und manuell triggern. es gibt keinen Anonymen modus. er darf eine gruppe also spiel löschen

3.3
der join link ist permanent gültig, nicht regenerierbar. mehrfach nutzbar aber nicht zeitlich bergenzt

Phase 4
4.1 
Die Frage soll immer Server Midnight erzeugt werden
4.2
Es soll das spiel eine gewisse zeitzone haben
4.3
Selbstvoting ist erlaubt, aber man darf sein vote nicht ändern, jeder darf nur eine person wählen, also single choice. die abtimmung ist anonym. es sollen real time ergebnisse sichtbar sien. das voting soll nciht nach ablauf editierbar sein
4.4
wenn der spieler nicht votes wird seine stimme einfach an diesem tag nicht verwendet. es soll sich nicht auf das ergenis auswirken

5.1.1 Eine Gruppe darf nicht die gleiche Frage zweimal bekommen
5.1.2 Eine Gruppe kann zufällig die gleiche Frage wie eine andere Gruppe bekommen
5.2.1 Pro Tag bekommt man genau eine Frage, die man beantworten muss
5.2.2 Nein, sie sind nicht gruppenspezifisch
5.3. Keiner kann fragen hinzufügen, sie kommen von uns vom backend

6.1 Das Resultat ist gleich sichtbar, es müssen nicht alle abgestimmt haben, man kann sich auch den derzeitigen Zwischenstand ansehen
7.1 Ja, es kommt eine Frage pro Tag
7.2 Alle Spieler die in einer Gruppe sind bekommen alle die gleiche Frage pro Tag
7.3 Alle Spieler, die in einer Gruppe sind, dürfen abstimmen 
8.1.1 Die Votes bleiben wenn ein Spieler die Gruppe verlässt
8.1.2 Die Votes sind anonymisiert, man weiß nicht wer für wen abgestimmt hat, nur weiß man, wie viele für eine Person abgestimmt haben
8.1.3 Votes werden nicht gelöscht
