package dreamdev.moniepoint.services;

import dreamdev.moniepoint.data.repositories.*;
import dreamdev.moniepoint.dtos.request.*;
import dreamdev.moniepoint.dtos.response.CastVoteResponse;
import dreamdev.moniepoint.dtos.response.NominateCandidateResponse;
import dreamdev.moniepoint.exceptions.CandidateNotFoundException;
import dreamdev.moniepoint.exceptions.ElectionNotFoundException;
import dreamdev.moniepoint.exceptions.InvalidVoteException;
import dreamdev.moniepoint.exceptions.VoterNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class VoteServiceImplTest {

    @Autowired private VoteService voteService;
    @Autowired private CitizenService citizenService;
    @Autowired private VoterService voterService;
    @Autowired private CandidateService candidateService;
    @Autowired private ElectionService electionService;

    @Autowired private VoteRepository voteRepository;
    @Autowired private CandidateRepository candidateRepository;
    @Autowired private ElectionTypeRepository electionTypeRepository;
    @Autowired private RegisteredVotersRepository registeredVotersRepository;
    @Autowired private CitizensRepository citizensRepository;

    private CastVoteRequest castVoteRequest;
    private String voterAId;
    private String voterBId;
    private String candidateId;
    private String electionTypeId;

    @BeforeEach
    public void setUp() {
        voteRepository.deleteAll();
        candidateRepository.deleteAll();
        electionTypeRepository.deleteAll();
        registeredVotersRepository.deleteAll();
        citizensRepository.deleteAll();

        CitizenRegistrationRequest citizenARequest = new CitizenRegistrationRequest();
        citizenARequest.setFirstName("Tunde");
        citizenARequest.setLastName("Bello");
        citizenARequest.setDateOfBirth("1992-08-15");
        citizenARequest.setPhoneNumber("08011111111");
        citizenARequest.setGender("Male");
        citizenARequest.setStateOfOrigin("Lagos");
        var citizenA = citizenService.register(citizenARequest);

        VotersRegistrationRequest voterARequest = new VotersRegistrationRequest();
        voterARequest.setNationalID(citizenA.getNationalID());
        voterARequest.setPassword("passA");
        var voterAResponse = voterService.register(voterARequest);
        voterAId = voterAResponse.getVoterID();

        CitizenRegistrationRequest citizenBRequest = new CitizenRegistrationRequest();
        citizenBRequest.setFirstName("Edet");
        citizenBRequest.setLastName("Okon");
        citizenBRequest.setDateOfBirth("1985-03-22");
        citizenBRequest.setPhoneNumber("08022222222");
        citizenBRequest.setGender("Female");
        citizenBRequest.setStateOfOrigin("Ogun");
        var citizenB = citizenService.register(citizenBRequest);

        VotersRegistrationRequest voterBRequest = new VotersRegistrationRequest();
        voterBRequest.setNationalID(citizenB.getNationalID());
        voterBRequest.setPassword("passB");
        var voterBResponse = voterService.register(voterBRequest);
        voterBId = voterBResponse.getVoterID();

        CreateElectionRequest electionRequest = new CreateElectionRequest();
        electionRequest.setName("Presidential 2026");
        electionRequest.setDescription("General election");
        var election = electionService.createElection(electionRequest);
        electionTypeId = election.getElectionTypeId();

        NominateCandidateRequest nominateRequest = new NominateCandidateRequest();
        nominateRequest.setVoterID(voterBId);
        nominateRequest.setElectionTypeId(electionTypeId);
        NominateCandidateResponse candidateResponse = candidateService.nominate(nominateRequest);
        candidateId = candidateResponse.getCandidateId();

        electionService.startElection(electionTypeId);

        VotersLoginRequest loginRequest = new VotersLoginRequest();
        loginRequest.setVoterID(voterAId);
        loginRequest.setPassword("passA");
        voterService.login(loginRequest);

        castVoteRequest = new CastVoteRequest();
        castVoteRequest.setVoterID(voterAId);
        castVoteRequest.setElectionTypeId(electionTypeId);
        castVoteRequest.setCandidateId(candidateId);
    }

    @Test
    public void castVote_success_voteIsSaved() {
        assertEquals(0L, voteRepository.count());
        voteService.castVote(castVoteRequest);
        assertEquals(1L, voteRepository.count());
    }

    @Test
    public void castVote_success_candidateVoteCountIsIncremented() {
        voteService.castVote(castVoteRequest);
        var candidate = candidateRepository.findById(candidateId).get();
        assertEquals(1, candidate.getVoteCount());
    }

    @Test
    public void castVote_success_voterHasVotedFlagIsTrue() {
        voteService.castVote(castVoteRequest);
        var voter = registeredVotersRepository.findByVoterID(voterAId).get();
        assertTrue(voter.getHasVoted().get(electionTypeId));
    }

    @Test
    public void castVote_success_responseContainsCorrectElectionName() {
        CastVoteResponse response = voteService.castVote(castVoteRequest);
        assertEquals("Presidential 2026", response.getElectionName());
    }

    @Test
    public void castVote_success_responseContainsVoterId() {
        CastVoteResponse response = voteService.castVote(castVoteRequest);
        assertEquals(voterAId, response.getVoterID());
    }

    @Test
    public void castVote_whenVoterIsNotLoggedIn_throwsInvalidVoteException() {
        voterService.logout(voterAId);
        assertThrows(InvalidVoteException.class, () -> voteService.castVote(castVoteRequest));
    }

    @Test
    public void castVote_whenVoterAlreadyVoted_throwsInvalidVoteException() {
        voteService.castVote(castVoteRequest);
        assertThrows(InvalidVoteException.class, () -> voteService.castVote(castVoteRequest));
    }

    @Test
    public void castVote_inClosedElection_throwsInvalidVoteException() {
        electionService.closeElection(electionTypeId);
        assertThrows(InvalidVoteException.class, () -> voteService.castVote(castVoteRequest));
    }

    @Test
    public void castVote_withInvalidElectionId_throwsElectionNotFoundException() {
        castVoteRequest.setElectionTypeId("INVALID_ELECTION_ID");
        assertThrows(ElectionNotFoundException.class, () -> voteService.castVote(castVoteRequest));
    }

    @Test
    public void castVote_withInvalidCandidateId_throwsCandidateNotFoundException() {
        castVoteRequest.setCandidateId("INVALID_CANDIDATE_ID");
        assertThrows(CandidateNotFoundException.class, () -> voteService.castVote(castVoteRequest));
    }

    @Test
    public void castVote_withInvalidVoterId_throwsVoterNotFoundException() {
        castVoteRequest.setVoterID("INVALID_VOTER_ID");
        assertThrows(VoterNotFoundException.class, () -> voteService.castVote(castVoteRequest));
    }

    @Test
    public void castVote_selfVote_throwsInvalidVoteException() {
        VotersLoginRequest loginB = new VotersLoginRequest();
        loginB.setVoterID(voterBId);
        loginB.setPassword("passB");
        voterService.login(loginB);

        CastVoteRequest selfVoteRequest = new CastVoteRequest();
        selfVoteRequest.setVoterID(voterBId);
        selfVoteRequest.setElectionTypeId(electionTypeId);
        selfVoteRequest.setCandidateId(candidateId);

        assertThrows(InvalidVoteException.class, () -> voteService.castVote(selfVoteRequest));
    }



