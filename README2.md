# Election Voting App

A RESTful election management system built with Spring Boot and MongoDB. The app models the full lifecycle of a democratic election — from registering citizens, enrolling voters, nominating candidates, running elections, casting votes, and viewing results.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 23 |
| Framework | Spring Boot 4.1.0-M3 |
| Database | MongoDB (Spring Data) |
| Build Tool | Maven |
| Boilerplate | Lombok |
| Testing | Spring Boot Test, JUnit 5, Mockito |

---

## Domain Model

The system is built around five core entities that reflect a real-world voting pipeline:

```
Citizen → RegisteredVoter → Candidate
                  ↓               ↓
               Vote  ←→  ElectionType
```

### Citizen
The base identity. Every person in the system starts as a citizen.

| Field | Type | Notes |
|---|---|---|
| id | String | MongoDB auto-generated |
| firstName | String | |
| lastName | String | |
| nationalID | String | Auto-generated (`NGN` + 10 digits) |
| phoneNumber | String | Must be exactly 11 digits |
| dateOfBirth | LocalDate | Format: `yyyy-MM-dd` |
| stateOfOrigin | String | Must be a valid Nigerian state |
| gender | String | |

### RegisteredVoter
A citizen who has enrolled to vote. Age must be 18+.

| Field | Type | Notes |
|---|---|---|
| id | String | |
| citizen | Citizen | `@DocumentReference` |
| nationalID | String | Mirrors citizen nationalID |
| voterID | String | Auto-generated (`AA000000` format) |
| password | String | Set at registration |
| registeredAt | LocalDateTime | |
| isLoggedIn | boolean | Default: `false` |
| hasVoted | Map<String, Boolean> | Key = electionTypeId — tracks vote status per election independently |

### ElectionType
An election created and managed by an admin.

| Field | Type | Notes |
|---|---|---|
| id | String | |
| name | String | e.g. "Presidential 2026" |
| description | String | |
| status | ElectionStatus | `PENDING` → `ACTIVE` → `CLOSED` |
| candidateIds | List\<String\> | IDs of nominated candidates |
| createdAt | LocalDateTime | |

### Candidate
A registered voter who has nominated themselves for a specific election.

| Field | Type | Notes |
|---|---|---|
| id | String | |
| registeredVoter | RegisteredVoter | `@DocumentReference` |
| electionTypeId | String | Which election they are running in |
| voteCount | int | Default: `0`, incremented on each vote |
| nominatedAt | LocalDateTime | |

### Vote
An immutable audit record of every vote cast.

| Field | Type | Notes |
|---|---|---|
| id | String | |
| voterId | String | VoterID of the voter who cast the vote |
| candidate | Candidate | `@DocumentReference` |
| candidateId | String | For direct queries without dereferencing |
| electionTypeId | String | Which election this vote belongs to |
| votedAt | LocalDateTime | |

---

## Project Structure

```
src/
├── main/java/dreamdev/moniepoint/
│   ├── Main.java
│   ├── controllers/
│   │   ├── CitizenController.java
│   │   ├── VotersController.java
│   │   ├── CandidateController.java
│   │   ├── ElectionController.java
│   │   └── VoteController.java
│   ├── services/
│   │   ├── CitizenService.java / CitizenServiceImpl.java
│   │   ├── VoterService.java / VoterServiceImp.java
│   │   ├── CandidateService.java / CandidateServiceImpl.java
│   │   ├── ElectionService.java / ElectionServiceImpl.java
│   │   └── VoteService.java / VoteServiceImpl.java
│   ├── data/
│   │   ├── models/
│   │   │   ├── Citizen.java
│   │   │   ├── RegisteredVoter.java
│   │   │   ├── Candidate.java
│   │   │   ├── ElectionType.java
│   │   │   ├── ElectionStatus.java
│   │   │   ├── Vote.java
│   │   │   └── StateEnum.java
│   │   └── repositories/
│   │       ├── CitizensRepository.java
│   │       ├── RegisteredVotersRepository.java
│   │       ├── CandidateRepository.java
│   │       ├── ElectionTypeRepository.java
│   │       └── VoteRepository.java
│   ├── dtos/
│   │   ├── request/
│   │   │   ├── CitizenRegistrationRequest.java
│   │   │   ├── VotersRegistrationRequest.java
│   │   │   ├── VotersLoginRequest.java
│   │   │   ├── NominateCandidateRequest.java
│   │   │   ├── CreateElectionRequest.java
│   │   │   └── CastVoteRequest.java
│   │   └── response/
│   │       ├── ApiResponse.java
│   │       ├── CitizenRegistrationResponse.java
│   │       ├── VotersRegistrationResponse.java
│   │       ├── VotersLoginResponse.java
│   │       ├── VotersLogoutResponse.java
│   │       ├── NominateCandidateResponse.java
│   │       ├── ElectionResponse.java
│   │       ├── ElectionResultsResponse.java
│   │       └── CastVoteResponse.java
│   ├── exceptions/
│   │   ├── VotingAppException.java
│   │   ├── AlreadyRegisteredException.java
│   │   ├── AlreadyANominatedCandidateException.java
│   │   ├── CandidateNotFoundException.java
│   │   ├── CitizenNotFoundException.java
│   │   ├── CitizenNotOfAgeException.java
│   │   ├── DuplicatePhoneNumberException.java
│   │   ├── ElectionNotFoundException.java
│   │   ├── IncorrectDatePattern.java
│   │   ├── InvalidElectionStateException.java
│   │   ├── InvalidLoginDetailsException.java
│   │   ├── InvalidPhoneNumberException.java
│   │   ├── InvalidStateException.java
│   │   ├── InvalidVoteException.java
│   │   └── VoterNotFoundException.java
│   └── utils/
│       ├── NationalIDGenerator.java
│       ├── VoterIDGenerator.java
│       ├── CitizensMapper.java
│       ├── VotersMapper.java
│       ├── CandidateMapper.java
│       ├── ElectionMapper.java
│       └── VoteMapper.java
└── test/java/dreamdev/moniepoint/
    ├── data/repositories/
    │   ├── CitizenRepositoryTest.java
    │   ├── RegisteredVoterRepositoryTest.java
    │   ├── CandidateRepositoryTest.java
    │   ├── ElectionTypeRepositoryTest.java
    │   └── VoteRepositoryTest.java
    ├── services/
    │   ├── CitizenServiceImplTest.java
    │   └── VoterServiceImplTest.java
    ├── controllers/
    │   └── CitizensControllerTest.java
    └── utils/
        ├── NationalIDGeneratorTest.java
        └── VotersIDGeneratorTest.java
```

---

## API Reference

All responses are wrapped in a standard envelope:

```json
{ "success": true, "data": { } }
{ "success": false, "data": "Error message here" }
```

### Citizens — `/citizens`

| Method | Endpoint | Description | Success |
|---|---|---|---|
| `POST` | `/citizens/register` | Register a new citizen | `201` |

**Request body:**
```json
{
  "firstName": "Emeka",
  "lastName": "Obi",
  "phoneNumber": "08012345678",
  "dateOfBirth": "1990-05-14",
  "stateOfOrigin": "Anambra",
  "gender": "Male"
}
```

---

### Voters — `/voters`

| Method | Endpoint | Description | Success |
|---|---|---|---|
| `POST` | `/voters/register` | Register a citizen as a voter (must be 18+) | `201` |
| `POST` | `/voters/login` | Voter login | `200` |
| `POST` | `/voters/logout/{voterID}` | Voter logout | `200` |

**Register request:**
```json
{
  "nationalID": "NGN1234567890",
  "password": "securePass123"
}
```

**Login request:**
```json
{
  "voterID": "AB123456",
  "password": "securePass123"
}
```

---

### Elections — `/election`

| Method | Endpoint | Description | Success |
|---|---|---|---|
| `POST` | `/election/create` | Admin creates a new election | `201` |
| `PATCH` | `/election/{electionTypeId}/start` | Set election `PENDING` → `ACTIVE` | `200` |
| `PATCH` | `/election/{electionTypeId}/close` | Set election `ACTIVE` → `CLOSED` | `200` |
| `GET` | `/election/{electionTypeId}/results` | Get results sorted by vote count (CLOSED only) | `200` |

**Create request:**
```json
{
  "name": "Presidential 2026",
  "description": "General presidential election"
}
```

---

### Candidates — `/candidate`

| Method | Endpoint | Description | Success |
|---|---|---|---|
| `POST` | `/candidate/nominate` | Registered voter nominates themselves (PENDING elections only) | `201` |
| `GET` | `/candidate/{electionTypeId}` | Get all candidates for an election | `200` |

**Nominate request:**
```json
{
  "voterID": "AB123456",
  "electionTypeId": "64abc123def456"
}
```

---

### Votes — `/vote`

| Method | Endpoint | Description | Success |
|---|---|---|---|
| `POST` | `/vote/cast` | Logged-in voter casts a vote (ACTIVE elections only) | `201` |

**Cast vote request:**
```json
{
  "voterID": "AB123456",
  "electionTypeId": "64abc123def456",
  "candidateId": "64xyz789ghi012"
}
```

---

## Election Lifecycle

```
[Admin] POST /election/create
              ↓
         Status: PENDING
         (candidates can now be nominated)
              ↓
[Admin] PATCH /election/{id}/start
              ↓
         Status: ACTIVE
         (voters can now cast votes)
              ↓
[Admin] PATCH /election/{id}/close
              ↓
         Status: CLOSED
         (results now available)
              ↓
[Anyone] GET /election/{id}/results
```

---

## Business Rules

| Rule | Detail |
|---|---|
| Age requirement | Voter must be at least 18 years old |
| Phone number | Must be exactly 11 digits |
| State of origin | Must be a valid Nigerian state |
| Nomination | Only allowed while election is `PENDING` |
| One nomination per election | A voter cannot nominate themselves twice in the same election |
| Cross-election nomination | A voter can be a candidate in multiple different elections |
| Voting | Only allowed while election is `ACTIVE` |
| Login required | Voter must be logged in to cast a vote |
| One vote per election | A voter cannot vote twice in the same election |
| Cross-election voting | `hasVoted` is tracked per election independently |
| No self-voting | A candidate cannot vote for themselves |
| Results | Only available once an election is `CLOSED` |

---

## Running the Project

### Prerequisites
- Java 23
- Maven
- MongoDB running locally on default port `27017`

### Start the application
```bash
mvn spring-boot:run
```

### Run tests
```bash
mvn test
```

---

## ID Formats

| ID | Format | Example |
|---|---|---|
| `nationalID` | `NGN` + 10 random digits | `NGN4823019571` |
| `voterID` | 2 uppercase letters + 6 digits | `AB123456` |
| MongoDB `id` | BSON ObjectId (auto) | `64abc123def456789012abcd` |
