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

    }

    @Test
    void testExistsByVoterIdAndElectionTypeId_Returns() {
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
    void testVoterCanVoteInDifferentElections_HasVotedTrackedIndependently() {
        Citizen citizen = buildAndSaveCitizen("Bob", "NGN1000000003", "08010000003");
        RegisteredVoter voter = buildAndSaveVoter(citizen, "VTR10003");
        Candidate candidateA = buildAndSaveCandidate(voter, "election-A");
        Candidate candidateB = buildAndSaveCandidate(voter, "election-B");

        buildAndSaveVote("VTR99004", candidateA, "election-A");
        buildAndSaveVote("VTR99004", candidateB, "election-B");

        assertThat(voteRepository.existsByVoterIdAndElectionTypeId("VTR99004", "election-A")).isTrue();

    }


    @Test
    void testFindAllByElectionTypeId_returnsEmpty_whenNoVotesCast() {
        List<Vote> votes = voteRepository.findAllByElectionTypeId("election-empty");
        assertThat(votes.size()).isEqualTo(0);
    }


    @Test
    void testCountByElectionTypeIdAndCandidateId_ReturnsZeroWhenNoVotes() {
        long count = voteRepository.countByElectionTypeIdAndCandidateId("election-empty", "candidate-none");
        assertThat(count).isEqualTo(0);
    }
}
