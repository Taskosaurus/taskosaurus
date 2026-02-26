# Functional Specification — Taskosaurus

**Project**: Taskosaurus
**Team**: Baumann Isabella, Gossenreiter Thomas, Grad Kinga
**Class**: 4ahitm – MEDT Swift
**Stack**: iOS (Swift) + Java/Quarkus backend (JPA/Hibernate, H2)

---

## 1. Overview

Taskosaurus digitizes party/social games for friend groups. The primary game in scope is **"Wer würde eher?"** ("Who would rather?"). A possible second game is **"Kiss Marry Kill"** *(scope unclear — not reflected in backend yet)*.

---

## 2. Game: Wer würde eher?

### 2.1 Concept

A group of friends receives one question per day, e.g.:
> *"Wer würde am ehesten ein absurdes Haustier adoptieren?"*

Each player votes for the group member they think best fits the question. After 24 hours, the results are revealed.

### 2.2 User Flow

1. **Create or join a group**
   - A player creates a group (gives it a name).
   - The backend generates a unique join `link` for the group.
   - The creator shares the group via QR code (others scan it to join).
   - A player can be a member of multiple groups simultaneously.

2. **Daily question**
   - Each group receives exactly one question per day.
   - The question is drawn from a global question pool (pre-seeded on the backend).
   - *(How the question is selected — random/automatic vs. manual — is TBD)*

3. **Voting**
   - Within a 24-hour window, each player selects one group member as their answer.
   - The answer options are all members of the group.
   - *(Whether self-voting, vote changes, or multiple votes are allowed is TBD)*

4. **Results**
   - After the 24-hour window closes, results are shown to all group members.
   - The result view shows: who voted for whom (aggregated vote counts per person).
   - Each player can also see whether they have already answered the question (`answered` flag).
   - **Nice to have**: Per-question anonymous mode (only vote counts shown, not who voted for whom).

### 2.3 Data Model (Backend)

| Entity | Key Fields | Notes |
|---|---|---|
| `Player` | `id`, `name` | A user of the app |
| `EntityGroup` | `id`, `name`, `link` | A friend group; `link` is the join URL/QR payload |
| `player_group` | `player_id`, `group_id` | Join table (many-to-many) |
| `Question` | `id`, `question` | Global pool of question texts |
| `GroupQuestion` | `id`, `question_id`, `group_id`, `date` | Assignment of a question to a group on a specific date |
| `GroupQuestionAnswer` | `id`, `answering_player_id`, `answer` (player_id), `group_question_id` | One player's vote: answeringPlayer voted for answer |

### 2.4 Key DTOs

| DTO | Purpose |
|---|---|
| `DailyQuestionResponseDto` | Returns today's question, `answered` flag, and aggregated vote counts |
| `DailyQuestionAnswerDto` | Submit a vote: `playerId`, `answerId`, `groupId`, `date` |
| `GroupQuestionAnswerCollectedDto` | Aggregated result: player name + vote count |
| `GroupQuestionAnswerDto` | Answer option: player id + name (for the voting selection screen) |
| `GroupNameDto` / `PlayerNameDto` | Create group / create player by name |

---

## 3. Open Questions

- **Kiss Marry Kill**: Is this game in scope? If yes, what is the game mechanic and does it need backend support?
- **Daily question trigger**: Automatic (random at midnight) or manual?
- **Voting rules**: Self-vote allowed? Vote changes allowed? One vote per player?
- **Results timing**: Shown only after 24h, or visible in real-time while voting is still open?
- **Authentication**: How is a player identified — local device storage of `playerId`, or a login system (Apple Sign-In, username/password)?
- **Group admin**: Does the group creator have any special role (e.g., can kick members, trigger questions)?
- **Question pool**: Pre-seeded only, or can players/admins add questions?

---

## 4. Out of Scope

- **Wahrheit oder Pflicht** — explicitly excluded.
