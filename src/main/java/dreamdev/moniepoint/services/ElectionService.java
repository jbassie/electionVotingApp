package dreamdev.moniepoint.services;

import dreamdev.moniepoint.dtos.request.CreateElectionRequest;
import dreamdev.moniepoint.dtos.response.ElectionResponse;
import dreamdev.moniepoint.dtos.response.ElectionResultsResponse;

public interface ElectionService {
    ElectionResponse createElection(CreateElectionRequest request);
    ElectionResponse startElection(String electionTypeId);
    ElectionResponse closeElection(String electionTypeId);
    ElectionResultsResponse getResults(String electionTypeId);
}
