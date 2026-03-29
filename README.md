The system is designed to mirror a real-world election pipeline by breaking it into clear, enforceable stages with strict rules at each step.

Identity First: Everything starts with a Citizen as the source of truth. This ensures that every voter and candidate is tied to a verified identity.
Controlled Access to Voting: Not all citizens can vote automatically. A citizen must explicitly register as a RegisteredVoter, enforcing eligibility rules like age (18+) and authentication (login/logout).
Election as a Lifecycle: Elections are modeled as a state machine (PENDING → ACTIVE → CLOSED) to control what actions are allowed at any given time:
PENDING: setup phase (candidate nomination)
ACTIVE: execution phase (voting)
CLOSED: final phase (results only)
Separation of Roles:
A RegisteredVoter can optionally become a Candidate, but only within the context of a specific election. This avoids global roles and keeps participation scoped per election.
Strict Validation Rules:
Each action is guarded by business constraints to maintain integrity:
No duplicate registrations or nominations
No voting outside the active phase
No multiple votes per election
No self-voting
Per-Election Isolation:
Voting and participation are tracked independently per election (e.g., hasVoted map), allowing the same user to engage in multiple elections without conflict.
Auditability & Transparency:
Votes are stored as immutable records, ensuring traceability, while results are derived only after the election is closed.