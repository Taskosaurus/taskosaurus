# Functional Specification — Taskosaurus
**Version**: 1.1
**Team**: Baumann Isabella, Gossenreiter Thomas, Grad Kinga
**Class**: 4ahitm – MEDT Swift
**Stack**: iOS (Swift/SwiftUI) · Android (Kotlin/Jetpack Compose) · Backend (Java/Quarkus, JPA/Hibernate, H2)

---

## 1. Global Time System

| Property | Value |
|---|---|
| Daily question creation | 00:00 UTC+1 (fixed offset) |
| Voting start | Immediately at creation |
| Voting end | `created_at + 24h` |
| Scheduler CRON | `0 0 * * *` (UTC+1 fixed) |

---

## 2. Account Model

### Player
| Field | Type | Notes |
|---|---|---|
| `id` | UUID | Primary key |
| `nickname` | String | Globally unique |
| `pin_hash` | String | Hashed PIN |
| `created_at` | Timestamp | |
| `deleted_at` | Timestamp (nullable) | Soft delete |

### PIN Rules
- Minimum 8 characters
- Changeable by the player

### Login Rate Limiting
| Attempts | Delay |
|---|---|
| 1–3 | None |
| 4–6 | 1 min |
| 7–9 | 2 min |
| 10–12 | 4 min |
| 13–15 | 8 min |
| > 15 | 1 attempt only |

### Login Error Response
Always returns `"User oder PIN falsch"` — no distinction between wrong nickname and wrong PIN.

---

## 3. Group Model

### Group
| Field | Type | Notes |
|---|---|---|
| `id` | UUID | Primary key |
| `name` | String | |
| `owner_id` | UUID | FK → Player |
| `join_link` | String | Permanent, reusable, not regeneratable |
| `created_at` | Timestamp | |
| `deleted_at` | Timestamp (nullable) | Soft delete |

### GroupMembership
| Field | Type | Notes |
|---|---|---|
| `id` | UUID | Primary key |
| `group_id` | UUID | FK → Group |
| `player_id` | UUID | FK → Player |
| `joined_at` | Timestamp | |
| `left_at` | Timestamp (nullable) | Set on leave/kick |

**Rejoin behaviour**: Creates a new `GroupMembership` record. The old membership record is kept.

---

## 4. Question System

### Question
| Field | Type | Notes |
|---|---|---|
| `id` | UUID | Primary key |
| `text` | String | The question text |

**Pool rules**:
- Different groups may receive the same question.
- A group may not receive the same question twice, unless the pool is exhausted.

### GroupQuestion
| Field | Type | Notes |
|---|---|---|
| `id` | UUID | Primary key |
| `group_id` | UUID | FK → Group |
| `question_id` | UUID | FK → Question |
| `membership_snapshot_id` | UUID | FK → snapshot set created at question time |
| `created_at` | Timestamp | |
| `closes_at` | Timestamp | `created_at + 24h` |

**Invariant**: Exactly 1 GroupQuestion per group per 24-hour window.

---

## 5. Membership Snapshot

At the moment a daily question is created, the current group membership is frozen into a snapshot.

### GroupMembershipSnapshot
| Field | Type | Notes |
|---|---|---|
| `id` | UUID | Primary key |
| `group_question_id` | UUID | FK → GroupQuestion |
| `player_id` | UUID | FK → Player |
| `membership_id` | UUID | FK → GroupMembership |
| `display_name` | String | Nickname at time of snapshot |

- Snapshot is created **only at question creation time**.
- Result aggregation is based **exclusively** on snapshot members.
- If a player leaves after snapshot creation, they remain in the snapshot (shown as "Ehemaliges Mitglied" in UI).

---

## 6. Voting

### Vote
| Field | Type | Notes |
|---|---|---|
| `id` | UUID | Primary key |
| `group_question_id` | UUID | FK → GroupQuestion |
| `answering_membership_id` | UUID | FK → GroupMembership (who is voting) |
| `target_snapshot_player_id` | UUID | FK → GroupMembershipSnapshot (who is voted for) |
| `created_at` | Timestamp | |

**Unique constraint**: `UNIQUE(answering_membership_id, group_question_id)`

### Voting Rules
| Rule | Value |
|---|---|
| Self-vote | Allowed |
| Vote change | Not allowed |
| Multiple votes | Not allowed |
| Anonymity | Voting is anonymous |
| Result visibility | Realtime aggregation (visible during open window) |
| After close | Not editable |
| Non-voters | Not counted in results |

---

## 7. Leave / Kick Behaviour

- `GroupMembership.left_at` is set.
- The membership's snapshot entry **remains**.
- Any votes cast **remain**.
- UI displays the player as **"Ehemaliges Mitglied"**.

---

## 8. Rejoin Behaviour

- A new `GroupMembership` record is created (new identity).
- Old votes remain attached to the old membership.
- The new membership can vote on future questions.

---

## 9. Result Aggregation

- Results are aggregated in **realtime** while the question is open.
- Aggregation is based **exclusively** on `GroupMembershipSnapshot` entries.
- Vote counts are shown per snapshot member.

---

## 10. Group Deletion

- **Soft delete**: `Group.deleted_at` is set to a non-null timestamp.
- **Cascade soft delete** applies to:
  - Memberships
  - GroupQuestions
  - Votes

---

## 11. Backend Invariants

| # | Invariant |
|---|---|
| I1 | Max. 1 open GroupQuestion per group at a time |
| I2 | Max. 1 vote per membership per GroupQuestion |
| I3 | Vote target must exist in the GroupMembershipSnapshot |
| I4 | Snapshot is created only at question creation time |
| I5 | Membership rejoin creates a new identity (new membership ID) |
| I6 | Aggregation is based only on snapshot members |
| I7 | Question is closed after 24h |

---

## 12. Current Implementation State

### 12.1 Backend (`backend/src/main`)

#### Entities (current)
| Entity | Fields |
|---|---|
| `Player` | `id` (Long), `name`, `groups` (ManyToMany) |
| `EntityGroup` | `id` (Long), `name`, `link`, `players` (ManyToMany) |
| `Question` | `id` (Long), `question` |
| `GroupQuestion` | `id` (Long), `question` (FK), `group` (FK), `date` (LocalDate) |
| `GroupQuestionAnswer` | `id` (Long), `answeringPlayer` (FK), `answer` (FK→Player), `groupQuestion` (FK) |

#### REST API (current)
| Method | Path | Description |
|---|---|---|
| GET | `/api/player/list` | All players |
| POST | `/api/player/create` | Create player (body: `{name}`) |
| GET | `/api/player/get/{id}` | Player by ID |
| GET | `/api/group/list` | All groups |
| POST | `/api/group/create/{name}` | Create group (body: Player) |
| GET | `/api/group/join/{id}/{playerName}` | Join group by link |
| POST | `/api/group/getJoinedGroups` | Groups for player (body: Player) |
| GET | `/api/question/list` | All questions |
| POST | `/api/question/getDailyQuestion` | Daily question for player+group |
| POST | `/api/question/answerDailyQuestion` | Submit answer |
| GET | `/api/groupquestion/list` | All group questions |
| GET | `/api/groupQuestionAnswer/list` | All answers |

#### Notable backend issues
- Join link hardcodes IP: `http://192.168.137.135:8080/api/group/join/{id}`
- Daily question assigned on-demand (first request of the day), not by scheduler
- No authentication, no session management
- No soft delete anywhere
- `GroupQuestionAnswer` has no unique constraint on (player, question)

---

### 12.2 iOS Frontend (`Frontend/IOS`)

#### Navigation structure
```
AppTabView
├── Tab 1: WhoWouldRatherView (home)
│   ├── GameSelectionView (group list)
│   │   └── GameView (vote / results)
│   │       └── GroupDetailView (members + QR invite)
│   └── GroupCreationView (create group)
└── Tab 2: SettingsView
    └── LoginRegisterView (login / register)
```

#### Models (current)
```swift
Player:         id (Int?), name (String)
Group:          id (Int?), name, link, players ([Player]?)
Question:       answered (Bool), date, question, answers ([CollectedAnswer])
CollectedAnswer: answeredId (Int), answeredName, count (Int)
Answer:         playerId, answerId, groupId, date (all Int/String)
```

#### Services (current)
| Service | Base URL | Calls |
|---|---|---|
| `PlayerService` | `192.168.137.135:8080/api/player` | createPlayer, getPlayer(id) |
| `GameService` | `192.168.137.135:8080` | fetchGroups, createGroup, joinGroup |
| `QuestionService` | `192.168.137.135:8080/api/question` | fetchDailyQuestion, answerDailyQuestion |

#### State management
- `ViewModel: ObservableObject` with `@Published` properties
- 5-second `Timer` polling for groups and questions
- `UserDefaults` stores `playerId` (Int), defaults to `1` if not set

#### Known issues / TODOs
- Login stores and retrieves a numeric `playerId` — no nickname/PIN
- `UserDefaults` default player ID is `1` (hardcoded fallback)
- Logout only prints to console, does not clear `UserDefaults`
- Password fields exist in `LoginRegisterView` but are not wired up
- QR scanning (camera + photo picker) implemented but join flow incomplete
- Base URL hardcoded to `192.168.137.135:8080`

---

### 12.3 Android Frontend (`frontend/Android`)

#### Navigation structure
```
MainScreen (NavHost)
├── "auth": LoginRegisterView
└── Games tab:
    ├── "title": TitleScreen (home)
    ├── "game_list": GameListScreen (group list)
    └── "game/{groupId}": GameScreen (vote / results)
Settings tab:
    └── SettingsScreen
        └── LoginRegisterView
```

#### Models (current)
```kotlin
Player:          id (Int), name (String)
Group:           id (Int), name, link, players (List<Player>)
Question:        answered (Bool), date, question, answers (List<CollectedAnswer>)
CollectedAnswer: answeredId (Int), answeredName, count (Int)
```

#### Network layer
- Retrofit with `OkHttpClient`
- Base URL: `10.0.2.2:8080` (emulator) / `192.168.137.135:8080` (device), auto-detected
- Three Retrofit interfaces: `PlayerApiService`, `GroupApiService`, `QuestionApiService`

#### State management
- `AndroidViewModel` with `StateFlow` properties
- `unAnsweredGroups` / `answeredGroups` computed from `latestQuestions`
- 5-second coroutine polling loop (`startAutoRefresh`)
- `SharedPreferences` (`PlayerPrefs`) stores `playerId` and `playerName`

#### Known issues / TODOs
- Login stores numeric player ID — no nickname/PIN
- `TitleScreen`: TODO — navigate to player list, not game view
- `GameListScreen`: TODO — QR code scanner not connected
- Base URL hardcoded (with emulator detection helper)
- Logout only clears local `SharedPreferences`, no server-side session

---

## 13. Gap Analysis — Spec vs. Current Implementation

### Backend
| Area | Current | Required |
|---|---|---|
| Player ID type | `Long` | UUID |
| Authentication | None | Nickname + PIN + rate limiting |
| Soft delete | None | Player, Group, Membership, GroupQuestion, Vote |
| GroupMembership | Raw join table (`player_group`) | Full entity with `joined_at`, `left_at` |
| Group owner | Not stored | `owner_id` FK on Group |
| MembershipSnapshot | Not implemented | `GroupMembershipSnapshot` entity |
| Vote entity | `GroupQuestionAnswer` (no UNIQUE constraint) | `Vote` with `UNIQUE(answering_membership_id, group_question_id)` |
| Question scheduling | On-demand (first request) | CRON `0 0 * * *` UTC+1 |
| Join link | Hardcoded IP | Token/UUID-based path |
| Realtime aggregation | Not implemented | Required |

### iOS
| Area | Current | Required |
|---|---|---|
| Auth flow | Numeric player ID | Nickname + PIN |
| ID types | `Int` | `String` (UUID) |
| Logout | Console print only | Clear `UserDefaults` |
| QR join flow | Partial (scan works, join incomplete) | Full end-to-end |
| Base URL | Hardcoded | Configurable |
| "Ehemaliges Mitglied" | Not handled | Show in vote/result UI |
| MembershipSnapshot model | Not present | Required for voting and results |

### Android
| Area | Current | Required |
|---|---|---|
| Auth flow | Numeric player ID | Nickname + PIN |
| ID types | `Int` | `String` (UUID) |
| QR scanner | TODO stub | Full end-to-end join flow |
| Base URL | Hardcoded (with emulator helper) | Configurable |
| "Ehemaliges Mitglied" | Not handled | Show in vote/result UI |
| MembershipSnapshot model | Not present | Required for voting and results |

---

## 14. Seed Data (import.sql)

- **14 questions** (German "Wer würde eher?" prompts)
- **4 groups**: Familie, 4AHITM, Die 5 Freunde, Die 3 ???
- **18 players**: Isabella, Max, Herbert, etc.
- **20 player–group memberships**
- **2 GroupQuestions** (today, groups 1 and 2)
- **12 sample answers**
