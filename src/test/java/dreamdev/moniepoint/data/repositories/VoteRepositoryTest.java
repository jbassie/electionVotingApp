package dreamdev.moniepoint.data.repositories;

import dreamdev.moniepoint.data.models.Candidate;
import dreamdev.moniepoint.data.models.Citizen;
import dreamdev.moniepoint.data.models.RegisteredVoter;
import dreamdev.moniepoint.data.models.Vote;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class VoteRepositoryTest {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private RegisteredVotersRepository registeredVotersRepository;

    @Autowired
    private CitizensRepository citizensRepository;

    @AfterEach
    void tearDown() {
        voteRepository.deleteAll();
        candidateRepository.deleteAll();
        registeredVotersRepository.deleteAll();
        citizensRepository.deleteAll();
    }

    private Citizen buildAndSaveCitizen(String firstName, String nationalID, String phone) {
        Citizen citizen = new Citizen();
        citizen.setFirstName(firstName);
        citizen.setLastName("Test");
        citizen.setNationalID(nationalID);
        citizen.setPhoneNumber(phone);
        citizen.setDateOfBirth(LocalDate.of(1990, 1, 1));
        citizen.setStateOfOrigin("LAGOS");
        citizen.setGender("Male");
        return citizensRepository.save(citizen);
    }

    private RegisteredVoter buildAndSaveVoter(Citizen citizen, String voterID) {
        RegisteredVoter voter = new RegisteredVoter();
        voter.setCitizen(citizen);
        voter.setNationalID(citizen.getNationalID());
        voter.setVoterID(voterID);
        voter.setPassword("password123");
        voter.setRegisteredAt(LocalDateTime.now());
        return registeredVotersRepository.save(voter);
    }

    private Candidate buildAndSaveCandidate(RegisteredVoter voter, String electionTypeId) {
        Candidate candidate = new Candidate();
        candidate.setRegisteredVoter(voter);
        candidate.setElectionTypeId(electionTypeId);
        candidate.setVoteCount(0);
        candidate.setNominatedAt(LocalDateTime.now());
        return candidateRepository.save(candidate);
    }

    private Vote buildAndSaveVote(String voterId, Candidate candidate, String electionTypeId) {
        Vote vote = new Vote();
        vote.setVoterId(voterId);
        vote.setCandidate(candidate);
        vote.setCandidateId(candidate.getId());
        vote.setElectionTypeId(electionTypeId);
        vote.setVotedAt(LocalDateTime.now());
        return voteRepository.save(vote);
    }

    @Test
    void newRepository_ReturnsZero() {
        assertEquals(0L, voteRepository.count());
    }

    @Test
    void testSaveVote() {
        Citizen citizen = buildAndSaveCitizen("John", "NGN1000000001", "08010000001");
        RegisteredVoter voter = buildAndSaveVoter(citizen, "VTR10001");
        Candidate candidate = buildAndSaveCandidate(voter, "election-100");

        Vote vote = new Vote();
        vote.setVoterId("VTR99001");
        vote.setCandidate(candidate);
        vote.setCandidateId(candidate.getId());
        vote.setElectionTypeId("election-100");
        vote.setVotedAt(LocalDateTime.now());

        Vote saved = voteRepository.save(vote);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getVoterId()).isEqualTo("VTR99001");
        assertThat(saved.getCandidate().getId()).isEqualTo(candidate.getId());
        assertThat(saved.getCandidateId()).isEqualTo(candidate.getId());
        assertThat(saved.getElectionTypeId()).isEqualTo("election-100");
        assertThat(saved.getVotedAt()).isNotNull();
    }

    @Test
    void testExistsByVoterIdAndElectionTypeId_true() {
        Citizen citizen = buildAndSaveCitizen("Alice", "NGN1000000002", "08010000002");
        RegisteredVoter voter = buildAndSaveVoter(citizen, "VTR10002");
        Candidate candidate = buildAndSaveCandidate(voter, "election-101");

        buildAndSaveVote("VTR99002", candidate, "election-101");

        boolean exists = voteRepository.existsByVoterIdAndElectionTypeId("VTR99002", "election-101");

        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByVoterIdAndElectionTypeId_false() {
        boolean exists = voteRepository.existsByVoterIdAndElectionTypeId("VTR99003", "election-102");
        assertThat(exists).isFalse();
    }

    @Test
    void testVoterCanVoteInDifferentElections_hasVotedTrackedIndependently() {
        Citizen citizen = buildAndSaveCitizen("Bob", "NGN1000000003", "08010000003");
        RegisteredVoter voter = buildAndSaveVoter(citizen, "VTR10003");
        Candidate candidateA = buildAndSaveCandidate(voter, "election-A");
        Candidate candidateB = buildAndSaveCandidate(voter, "election-B");

        buildAndSaveVote("VTR99004", candidateA, "election-A");
        buildAndSaveVote("VTR99004", candidateB, "election-B");

        assertThat(voteRepository.existsByVoterIdAndElectionTypeId("VTR99004", "election-A")).isTrue();
        assertThat(voteRepository.existsByVoterIdAndElectionTypeId("VTR99004", "election-B")).isTrue();
        assertThat(voteRepository.existsByVoterIdAndElectionTypeId("VTR99004", "election-C")).isFalse();
    }

    @Test
    void testFindAllByElectionTypeId_returnsAllVotesForElection() {
        Citizen c1 = buildAndSaveCitizen("Carol", "NGN1000000004", "08010000004");
        Citizen c2 = buildAndSaveCitizen("Dan", "NGN1000000005", "08010000005");
        RegisteredVoter v1 = buildAndSaveVoter(c1, "VTR10004");
        RegisteredVoter v2 = buildAndSaveVoter(c2, "VTR10005");
        Candidate candidate = buildAndSaveCandidate(v1, "election-103");

        buildAndSaveVote(v1.getVoterID(), candidate, "election-103");
        buildAndSaveVote(v2.getVoterID(), candidate, "election-103");

        List<Vote> votes = voteRepository.findAllByElectionTypeId("election-103");

        assertThat(votes.size()).isEqualTo(2);
        assertThat(votes.get(0).getElectionTypeId()).isEqualTo("election-103");
        assertThat(votes.get(1).getElectionTypeId()).isEqualTo("election-103");
    }

    @Test
    void testFindAllByElectionTypeId_returnsEmpty_whenNoVotesCast() {
        List<Vote> votes = voteRepository.findAllByElectionTypeId("election-empty");
        assertThat(votes.size()).isEqualTo(0);
    }

    @Test
    void testCountByElectionTypeIdAndCandidateId_returnsCorrectCount() {
        Citizen c1 = buildAndSaveCitizen("Eve", "NGN1000000006", "08010000006");
        Citizen c2 = buildAndSaveCitizen("Frank", "NGN1000000007", "08010000007");
        Citizen c3 = buildAndSaveCitizen("Grace", "NGN1000000008", "08010000008");
        RegisteredVoter v1 = buildAndSaveVoter(c1, "VTR10006");
        RegisteredVoter v2 = buildAndSaveVoter(c2, "VTR10007");
        RegisteredVoter v3 = buildAndSaveVoter(c3, "VTR10008");

        Candidate candidateX = buildAndSaveCandidate(v1, "election-104");
        Candidate candidateY = buildAndSaveCandidate(v2, "election-104");

        buildAndSaveVote(v1.getVoterID(), candidateX, "election-104");
        buildAndSaveVote(v2.getVoterID(), candidateX, "election-104");
        buildAndSaveVote(v3.getVoterID(), candidateY, "election-104");

        long countX = voteRepository.countByElectionTypeIdAndCandidateId("election-104", candidateX.getId());
        long countY = voteRepository.countByElectionTypeIdAndCandidateId("election-104", candidateY.getId());

        assertThat(countX).isEqualTo(2);
        assertThat(countY).isEqualTo(1);
    }

    @Test
    void testCountByElectionTypeIdAndCandidateId_returnsZero_whenNoVotes() {
        long count = voteRepository.countByElectionTypeIdAndCandidateId("election-empty", "candidate-none");
        assertThat(count).isEqualTo(0);
    }
}
