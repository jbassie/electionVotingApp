package dreamdev.moniepoint.utils;

import dreamdev.moniepoint.data.models.Candidate;
import dreamdev.moniepoint.data.models.Citizen;
import dreamdev.moniepoint.data.models.ElectionType;
import dreamdev.moniepoint.data.models.RegisteredVoter;
import dreamdev.moniepoint.dtos.response.ElectionResponse;
import dreamdev.moniepoint.dtos.response.ElectionResultsResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ElectionMapper {

    public static ElectionResponse map(ElectionType election) {
        ElectionResponse response = new ElectionResponse();
        response.setElectionTypeId(election.getId());
        response.setName(election.getName());
        response.setDescription(election.getDescription());
        response.setType(election.getType());
        response.setStatus(election.getStatus());
        response.setCandidateIds(election.getCandidateIds() != null ? election.getCandidateIds() : new ArrayList<>());
        response.setStartDate(election.getStartDate());
        response.setEndDate(election.getEndDate());
        response.setCreatedAt(election.getCreatedAt());
        return response;
    }

    public static ElectionResultsResponse mapToResults(ElectionType election, List<Candidate> candidates) {
        ElectionResultsResponse response = new ElectionResultsResponse();
        response.setElectionTypeId(election.getId());
        response.setElectionName(election.getName());
        response.setStatus(election.getStatus());

        List<ElectionResultsResponse.CandidateResultItem> items = candidates.stream()
                .map(candidate -> {
                    RegisteredVoter voter = candidate.getRegisteredVoter();
                    Citizen citizen = voter.getCitizen();

                    ElectionResultsResponse.CandidateResultItem item = new ElectionResultsResponse.CandidateResultItem();
                    item.setCandidateId(candidate.getId());
                    item.setFirstName(citizen.getFirstName());
                    item.setLastName(citizen.getLastName());
                    item.setStateOfOrigin(citizen.getStateOfOrigin());
                    item.setVoteCount(candidate.getVoteCount());
                    return item;
                })
                .collect(Collectors.toList());

        response.setCandidates(items);
        return response;
    }

}
