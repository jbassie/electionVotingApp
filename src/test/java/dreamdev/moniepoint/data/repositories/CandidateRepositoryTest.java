package dreamdev.moniepoint.data.repositories;

import dreamdev.moniepoint.data.models.Candidate;
import dreamdev.moniepoint.data.models.Citizen;
import dreamdev.moniepoint.data.models.RegisteredVoter;
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
public class CandidateRepositoryTest {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private RegisteredVotersRepository registeredVotersRepository;

    @Autowired
    private CitizensRepository citizensRepository;

    @AfterEach
    void tearDown() {
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

    private Candidate buildAndSaveCandidate(RegisteredVoter voter, String electionTypeId, int voteCount) {
        Candidate candidate = new Candidate();
        candidate.setRegisteredVoter(voter);
        candidate.setElectionTypeId(electionTypeId);
        candidate.setVoteCount(voteCount);
        candidate.setNominatedAt(LocalDateTime.now());
        return candidateRepository.save(candidate);
    }

    @Test
    void newRepository_ReturnsZero() {

        assertEquals(0L, candidateRepository.count());
    }

    @Test
    void testSaveCandidate_RepositoryIsNotNull() {
        Citizen citizen = buildAndSaveCitizen("John", "NGN1111111111", "08011111111");
        RegisteredVoter voter = buildAndSaveVoter(citizen, "VTR00001");

        Candidate candidate = new Candidate();
        candidate.setRegisteredVoter(voter);
        candidate.setElectionTypeId("election-001");
        candidate.setNominatedAt(LocalDateTime.now());

        Candidate saved = candidateRepository.save(candidate);

        assertThat(saved.getId()).isNotNull();

    }


    @Test
    void testFindByElectionTypeId_ReturnsCorrectCandidate() {
        Citizen citizen1 = buildAndSaveCitizen("Alice", "NGN2222222222", "08022222222");
        RegisteredVoter voter1 = buildAndSaveVoter(citizen1, "VTR00002");
        buildAndSaveCandidate(voter1, "election-002", 0);


        List<Candidate> candidates = candidateRepository.findByElectionTypeId("election-002");

        assertThat(candidates.get(0).getElectionTypeId()).isEqualTo("election-002");

    }


    @Test
    void testFindByElectionTypeIdForMultipleCandidates_ReturnsCorrectCount() {
        Citizen citizen1 = buildAndSaveCitizen("Alice", "NGN2222222222", "08022222222");
        Citizen citizen2 = buildAndSaveCitizen("Bob", "NGN3333333333", "08033333333");
        RegisteredVoter voter1 = buildAndSaveVoter(citizen1, "VTR00002");
        RegisteredVoter voter2 = buildAndSaveVoter(citizen2, "VTR00003");

        buildAndSaveCandidate(voter1, "election-002", 0);
        buildAndSaveCandidate(voter2, "election-002", 0);

        List<Candidate> candidates = candidateRepository.findByElectionTypeId("election-002");

        assertThat(candidates.size()).isEqualTo(2);

    }

    @Test
    void testFindByElectionTypeId_returnsEmpty_whenNoMatch() {
        List<Candidate> candidates = candidateRepository.findByElectionTypeId("non-existent-election");
        assertThat(candidates.size()).isEqualTo(0);
    }

    @Test
    void testExistsByRegisteredVoterAndElectionTypeId_ReturnsTrue() {
        Citizen citizen = buildAndSaveCitizen("Carol", "NGN4444444444", "08044444444");
        RegisteredVoter voter = buildAndSaveVoter(citizen, "VTR00004");
        buildAndSaveCandidate(voter, "election-003", 0);

        boolean exists = candidateRepository.existsByRegisteredVoterAndElectionTypeId(voter, "election-003");

        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByRegisteredVoterAndElectionTypeId_ReturnsFalse() {
        Citizen citizen = buildAndSaveCitizen("Dave", "NGN5555555555", "08055555555");
        RegisteredVoter voter = buildAndSaveVoter(citizen, "VTR00005");

        boolean exists = candidateRepository.existsByRegisteredVoterAndElectionTypeId(voter, "election-003");

        assertThat(exists).isFalse();
    }

