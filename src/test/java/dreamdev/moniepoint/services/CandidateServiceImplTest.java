package dreamdev.moniepoint.services;

import dreamdev.moniepoint.data.repositories.CandidateRepository;
import dreamdev.moniepoint.data.repositories.CitizensRepository;
import dreamdev.moniepoint.data.repositories.ElectionTypeRepository;
import dreamdev.moniepoint.data.repositories.RegisteredVotersRepository;
import dreamdev.moniepoint.dtos.request.CitizenRegistrationRequest;
import dreamdev.moniepoint.dtos.request.CreateElectionRequest;
import dreamdev.moniepoint.dtos.request.NominateCandidateRequest;
import dreamdev.moniepoint.dtos.request.VotersRegistrationRequest;
import dreamdev.moniepoint.dtos.response.NominateCandidateResponse;
import dreamdev.moniepoint.exceptions.AlreadyANominatedCandidateException;
import dreamdev.moniepoint.exceptions.ElectionNotFoundException;
import dreamdev.moniepoint.exceptions.InvalidElectionStateException;
import dreamdev.moniepoint.exceptions.VoterNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CandidateServiceImplTest {

    @Autowired private CandidateService candidateService;
    @Autowired private ElectionService electionService;
    @Autowired private CitizenService citizenService;
    @Autowired private VoterService voterService;

    @Autowired private CandidateRepository candidateRepository;
    @Autowired private ElectionTypeRepository electionTypeRepository;
    @Autowired private CitizensRepository citizensRepository;
    @Autowired private RegisteredVotersRepository registeredVotersRepository;

    private NominateCandidateRequest nominateRequest;
    private String electionTypeId;
    private String voterID;

    @BeforeEach
    public void setUp() {
        candidateRepository.deleteAll();
        electionTypeRepository.deleteAll();
        registeredVotersRepository.deleteAll();
        citizensRepository.deleteAll();

        CitizenRegistrationRequest citizenRequest = new CitizenRegistrationRequest();
        citizenRequest.setFirstName("Edet");
        citizenRequest.setLastName("Okon");
        citizenRequest.setDateOfBirth("1990-04-10");
        citizenRequest.setPhoneNumber("08011111111");
        citizenRequest.setGender("Male");
        citizenRequest.setStateOfOrigin("Anambra");
        var citizenResponse = citizenService.register(citizenRequest);

        VotersRegistrationRequest voterRequest = new VotersRegistrationRequest();
        voterRequest.setNationalID(citizenResponse.getNationalID());
        voterRequest.setPassword("pass123");
        var voterResponse = voterService.register(voterRequest);
        voterID = voterResponse.getVoterID();

        CreateElectionRequest electionRequest = new CreateElectionRequest();
        electionRequest.setName("Presidential 2026");
        electionRequest.setDescription("General election");
        var electionResponse = electionService.createElection(electionRequest);
        electionTypeId = electionResponse.getElectionTypeId();

        nominateRequest = new NominateCandidateRequest();
        nominateRequest.setVoterID(voterID);
        nominateRequest.setElectionTypeId(electionTypeId);
    }

    @Test
    public void nominate_success_candidateIsSavedToRepository() {
        assertEquals(0L, candidateRepository.count());
        candidateService.nominate(nominateRequest);
        assertEquals(1L, candidateRepository.count());
    }

    @Test
    public void nominate_success_electionCandidateIdListIsUpdated() {
        candidateService.nominate(nominateRequest);
        var election = electionTypeRepository.findById(electionTypeId).get();
        assertEquals(1, election.getCandidateIds().size());
    }

    @Test
    public void nominate_success_returnedCandidateIdIsNotNull() {
        NominateCandidateResponse response = candidateService.nominate(nominateRequest);
        assertNotNull(response.getCandidateId());
    }

    @Test
    public void nominate_success_returnedFirstNameMatchesCitizen() {
        NominateCandidateResponse response = candidateService.nominate(nominateRequest);
        assertEquals("Edet", response.getFirstName());
    }

    @Test
    public void nominate_withInvalidVoterId_throwsVoterNotFoundException() {
        nominateRequest.setVoterID("INVALID_VOTER_ID");
        assertThrows(VoterNotFoundException.class, () -> candidateService.nominate(nominateRequest));
    }

    @Test
    public void nominate_withInvalidElectionId_throwsElectionNotFoundException() {
        nominateRequest.setElectionTypeId("INVALID_ELECTION_ID");
        assertThrows(ElectionNotFoundException.class, () -> candidateService.nominate(nominateRequest));
    }

    @Test
    public void nominate_whenElectionIsActive_throwsInvalidElectionStateException() {
        electionService.startElection(electionTypeId);
        assertThrows(InvalidElectionStateException.class, () -> candidateService.nominate(nominateRequest));
    }

    @Test
    public void nominate_whenElectionIsClosed_throwsInvalidElectionStateException() {
        electionService.startElection(electionTypeId);
        electionService.closeElection(electionTypeId);
        assertThrows(InvalidElectionStateException.class, () -> candidateService.nominate(nominateRequest));
    }

    @Test
    public void nominateSameVoterTwiceInSameElection_throwsAlreadyANominatedCandidateException() {
        candidateService.nominate(nominateRequest);
        assertThrows(AlreadyANominatedCandidateException.class, () -> candidateService.nominate(nominateRequest));
    }

    @Test
    public void nominate_sameVoterInTwoDifferentElections_bothSucceed() {
        CreateElectionRequest secondElectionRequest = new CreateElectionRequest();
        secondElectionRequest.setName("Gubernatorial 2026");
        secondElectionRequest.setDescription("State election");
        var secondElection = electionService.createElection(secondElectionRequest);

        candidateService.nominate(nominateRequest);

        NominateCandidateRequest secondNominateRequest = new NominateCandidateRequest();
        secondNominateRequest.setVoterID(voterID);
        secondNominateRequest.setElectionTypeId(secondElection.getElectionTypeId());

        assertDoesNotThrow(() -> candidateService.nominate(secondNominateRequest));
    }

    @Test
    public void nominate_sameVoterInTwoDifferentElections_candidateCountIsTwo() {
        CreateElectionRequest secondElectionRequest = new CreateElectionRequest();
        secondElectionRequest.setName("Gubernatorial 2026");
        secondElectionRequest.setDescription("State election");
        var secondElection = electionService.createElection(secondElectionRequest);

        candidateService.nominate(nominateRequest);

        NominateCandidateRequest secondNominateRequest = new NominateCandidateRequest();
        secondNominateRequest.setVoterID(voterID);
        secondNominateRequest.setElectionTypeId(secondElection.getElectionTypeId());
        candidateService.nominate(secondNominateRequest);

        assertEquals(2L, candidateRepository.count());
    }

    @Test
    public void getCandidatesByElection_returnsCandidatesForCorrectElection() {
        CitizenRegistrationRequest secondCitizenRequest = new CitizenRegistrationRequest();
        secondCitizenRequest.setFirstName("Chioma");
        secondCitizenRequest.setLastName("Eze");
        secondCitizenRequest.setDateOfBirth("1988-07-22");
        secondCitizenRequest.setPhoneNumber("08022222222");
        secondCitizenRequest.setGender("Female");
        secondCitizenRequest.setStateOfOrigin("Imo");
        var secondCitizen = citizenService.register(secondCitizenRequest);

        VotersRegistrationRequest secondVoterRequest = new VotersRegistrationRequest();
        secondVoterRequest.setNationalID(secondCitizen.getNationalID());
        secondVoterRequest.setPassword("pass456");
        var secondVoter = voterService.register(secondVoterRequest);

        candidateService.nominate(nominateRequest);

        NominateCandidateRequest secondNominateRequest = new NominateCandidateRequest();
        secondNominateRequest.setVoterID(secondVoter.getVoterID());
        secondNominateRequest.setElectionTypeId(electionTypeId);
        candidateService.nominate(secondNominateRequest);

        List<NominateCandidateResponse> candidates = candidateService.getCandidatesByElection(electionTypeId);
        assertEquals(2, candidates.size());
    }

    @Test
    public void getCandidatesByElection_withInvalidElectionId_throwsElectionNotFoundException() {
        assertThrows(ElectionNotFoundException.class,
                () -> candidateService.getCandidatesByElection("INVALID_ELECTION_ID"));
    }

    @Test
    public void nominate_success_otherCandidatesListIsEmptyForFirstCandidate() {
        NominateCandidateResponse response = candidateService.nominate(nominateRequest);
        assertTrue(response.getOtherCandidateNames().isEmpty());
    }

}
