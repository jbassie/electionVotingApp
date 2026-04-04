package dreamdev.moniepoint.services;

import dreamdev.moniepoint.data.models.ElectionStatus;
import dreamdev.moniepoint.data.repositories.CandidateRepository;
import dreamdev.moniepoint.data.repositories.ElectionTypeRepository;
import dreamdev.moniepoint.dtos.request.CreateElectionRequest;
import dreamdev.moniepoint.dtos.response.ElectionResponse;
import dreamdev.moniepoint.dtos.response.ElectionResultsResponse;
import dreamdev.moniepoint.exceptions.ElectionNotFoundException;
import dreamdev.moniepoint.exceptions.InvalidElectionStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ElectionServiceImplTest {

    @Autowired private ElectionService electionService;

    @Autowired private ElectionTypeRepository electionTypeRepository;
    @Autowired private CandidateRepository candidateRepository;

    private CreateElectionRequest createRequest;

    @BeforeEach
    public void setUp() {
        candidateRepository.deleteAll();
        electionTypeRepository.deleteAll();

        createRequest = new CreateElectionRequest();
        createRequest.setName("Presidential 2026");
        createRequest.setDescription("General election for president");
    }

    @Test
    public void createElection_success_electionIsSaved() {
        assertEquals(0L, electionTypeRepository.count());
        electionService.createElection(createRequest);
        assertEquals(1L, electionTypeRepository.count());
    }

    @Test
    public void createElection_defaultStatusIsPending() {
        ElectionResponse response = electionService.createElection(createRequest);
        assertEquals(ElectionStatus.PENDING, response.getStatus());
    }

    @Test
    public void createElection_candidateIdsListIsEmpty() {
        ElectionResponse response = electionService.createElection(createRequest);
        assertTrue(response.getCandidateIds().isEmpty());
    }

    @Test
    public void createElection_returnedNameMatchesRequest() {
        ElectionResponse response = electionService.createElection(createRequest);
        assertEquals("Presidential 2026", response.getName());
    }

    @Test
    public void createElection_returnedIdIsNotNull() {
        ElectionResponse response = electionService.createElection(createRequest);
        assertNotNull(response.getElectionTypeId());
    }

    @Test
    public void startElection_statusChangesToActive() {
        var created = electionService.createElection(createRequest);
        ElectionResponse response = electionService.startElection(created.getElectionTypeId());
        assertEquals(ElectionStatus.ACTIVE, response.getStatus());
    }

    @Test
    public void startElection_persistsStatusChangeInRepository() {
        var created = electionService.createElection(createRequest);
        electionService.startElection(created.getElectionTypeId());
        var fromRepo = electionTypeRepository.findById(created.getElectionTypeId()).get();
        assertEquals(ElectionStatus.ACTIVE, fromRepo.getStatus());
    }

    @Test
    public void startAlreadyActiveElection_throwsInvalidElectionStateException() {
        var created = electionService.createElection(createRequest);
        electionService.startElection(created.getElectionTypeId());
        assertThrows(InvalidElectionStateException.class,
                () -> electionService.startElection(created.getElectionTypeId()));
    }



    @Test
    public void closeElection_persistsStatusChangeInRepository() {
        var created = electionService.createElection(createRequest);
        electionService.startElection(created.getElectionTypeId());
        electionService.closeElection(created.getElectionTypeId());
        var fromRepo = electionTypeRepository.findById(created.getElectionTypeId()).get();
        assertEquals(ElectionStatus.CLOSED, fromRepo.getStatus());
    }

    @Test
    public void closePendingElection_throwsInvalidElectionStateException() {
        var created = electionService.createElection(createRequest);
        assertThrows(InvalidElectionStateException.class,
                () -> electionService.closeElection(created.getElectionTypeId()));
    }

    @Test
    public void closeNonExistentElection_throwsElectionNotFoundException() {
        assertThrows(ElectionNotFoundException.class,
                () -> electionService.closeElection("INVALID_ELECTION_ID"));
    }

    @Test
    public void getResults_withNonExistentElection_throwsElectionNotFoundException() {
        assertThrows(ElectionNotFoundException.class,
                () -> electionService.getResults("INVALID_ELECTION_ID"));
    }

    @Test
    public void getResults_withPendingElection_throwsInvalidElectionStateException() {
        var created = electionService.createElection(createRequest);
        assertThrows(InvalidElectionStateException.class,
                () -> electionService.getResults(created.getElectionTypeId()));
    }



    @Test
    public void getResults_withClosedElection_statusIsClosed() {
        var created = electionService.createElection(createRequest);
        electionService.startElection(created.getElectionTypeId());
        electionService.closeElection(created.getElectionTypeId());
        ElectionResultsResponse results = electionService.getResults(created.getElectionTypeId());
        assertEquals(ElectionStatus.CLOSED, results.getStatus());
    }

}
