Perfekt 👍 — genau **so** sollte man Codex füttern: mit einer iterativ geschärften Functional Spec.

Du hast schon eine gute **Feature-Idee**, aber aktuell fehlt noch fast alles, was Codex braucht, um:

* Datenkonsistenz zu garantieren
* Edge-Cases korrekt zu behandeln
* APIs deterministisch zu implementieren
* Race-Conditions zu vermeiden
* Security sauber zu modellieren
* Client/Server-Responsibility zu trennen

👉 Ich werde **keine Annahmen treffen**.
👉 Ich werde systematisch alle impliziten Entscheidungen explizit machen.
👉 Wir gehen Domain-Driven von außen nach innen:

```
Product Rules → Game Rules → Identity → Time → Authority → Data Ownership → APIs → State Machine
```

---

# 🔴 PHASE 1 — PRODUCT BOUNDARIES

### 1.1 Zielplattform

Bitte beantworten:

1. Soll es **nur iOS** geben oder auch Android später?
2. Muss der Backend-API-Vertrag:

   * a) versioniert werden?
   * b) offline-fähig sein?
3. Muss ein Spieler dieselbe Gruppe auf **mehreren Geräten gleichzeitig** verwenden können?

---

# 🔴 PHASE 2 — USER IDENTITY (KRITISCH)

Aktuell existiert:

```
Player { id, name }
```

Das ist **keine Identität**, sondern nur ein Anzeigename.

Bitte festlegen:

### 2.1 Accountmodell

Welche der folgenden Optionen gilt?

A
Spieler = Gerät
→ Identity wird lokal gespeichert
→ kein Login
→ Neuinstallation = neuer Spieler

B
Spieler = Account
→ Login erforderlich
→ Geräteübergreifend

C
Hybrid
→ Gastmodus + optionaler Account

---

### 2.2 Falls Login existiert:

Welche Authentifizierung?

* Apple Sign In
* E-Mail + Passwort
* Magic Link
* Nickname + PIN
* Keine

---

### 2.3 Darf ein Spieler:

| Aktion                    | Ja / Nein |
| ------------------------- | --------- |
| Gruppen verlassen         |           |
| Gruppen wieder beitreten  |           |
| Seinen Namen ändern       |           |
| Mehrere Accounts besitzen |           |

---

# 🔴 PHASE 3 — GROUP AUTHORITY MODEL

Aktuell:

> Creator erstellt Gruppe

Aber:

### 3.1 Existieren Rollen?

* Owner
* Admin
* Member
* Keine Rollen

---

### 3.2 Falls Owner existiert:

Darf Owner:

| Aktion                    | Ja / Nein |
| ------------------------- | --------- |
| Mitglieder kicken         |           |
| Mitglieder bannen         |           |
| Gruppennamen ändern       |           |
| Frage überspringen        |           |
| Frage manuell triggern    |           |
| Anonymen Modus aktivieren |           |
| Spiel löschen             |           |

---

### 3.3 Join Link Verhalten

Ist der Join-Link:

* permanent gültig?
* regenerierbar?
* einmalig nutzbar?
* zeitlich begrenzt?

---

# 🔴 PHASE 4 — GAME RULES

Jetzt wichtig: **exakte Spielmechanik**

---

## 4.1 Daily Question Lifecycle

Wann wird eine Frage erzeugt?

* a) Server-Midnight (UTC?)
* b) Group-Local-Timezone Midnight
* c) Beim ersten Öffnen der App am Tag
* d) Manuell durch Admin
* e) Scheduler pro Gruppe

---

## 4.2 Falls Zeitzone:

Hat jede Gruppe:

* fixe Zeitzone?
* Zeitzone des Owners?
* dynamisch berechnet?

---

## 4.3 Voting Rules

Bitte festlegen:

| Regel                         | Auswahl |
| ----------------------------- | ------- |
| Selbst-Voting erlaubt         |         |
| Vote änderbar                 |         |
| Max Votes pro Spieler         |         |
| Pflicht zu voten              |         |
| Abstimmung anonym             |         |
| Real-Time Ergebnisse sichtbar |         |
| Voting vor Ablauf sichtbar    |         |
| Voting nach Ablauf editierbar |         |

---

## 4.4 Wenn ein Spieler nicht votet

Nach 24h:

* Wird er als „0 Stimmen“ gewertet?
* Wird er aus Aggregation entfernt?
* Zählt er als "Skipped"?

---

# 🔴 PHASE 5 — QUESTION ASSIGNMENT

### 5.1 Darf eine Gruppe:

* dieselbe Frage zweimal bekommen?
* dieselbe Frage wie andere Gruppen bekommen?

---

### 5.2 Müssen Fragen:

* global eindeutig pro Tag sein?
* gruppenspezifisch sein?

---

### 5.3 Fragepool Erweiterung

Darf:

| Rolle           | Fragen hinzufügen |
| --------------- | ----------------- |
| Backend Admin   |                   |
| Group Owner     |                   |
| Alle Mitglieder |                   |

---

# 🔴 PHASE 6 — RESULT STATE MACHINE

Aktuell fehlt eine Zustandslogik.

Soll es geben:

```
CREATED
OPEN
CLOSED
RESULT_READY
ARCHIVED
```

Oder:

* OPEN
* CLOSED

---

### 6.1 Wann wird Result sichtbar?

* exakt nach 24h?
* wenn alle gevotet haben?
* manueller Reveal?

---

# 🔴 PHASE 7 — MULTI GROUP EDGE CASE

Spieler ist in:

```
Group A
Group B
Group C
```

Bitte festlegen:

1. Eine Frage pro Gruppe pro Tag?
2. Oder global nur eine Frage pro Spieler?
3. Darf ein Spieler in Gruppe A bereits voten und in B nicht?

---

# 🔴 PHASE 8 — DELETION RULES

Was passiert wenn:

### 8.1 Spieler verlässt Gruppe?

* Votes bleiben?
* Votes werden anonymisiert?
* Votes werden gelöscht?

---

### 8.2 Spieler Account gelöscht?

---

Bitte beantworte **alle Fragen nummeriert** (z.B. 1.1, 2.1, 2.2 …).
Danach erstelle ich mit dir:

✅ Zustandsdiagramm
✅ API-Contract
✅ Backend Invariants
✅ Conflict Rules
✅ Final Codex-Ready Spec
