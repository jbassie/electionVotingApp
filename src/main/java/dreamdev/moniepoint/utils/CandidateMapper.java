package dreamdev.moniepoint.utils;

import dreamdev.moniepoint.data.models.Candidate;
import dreamdev.moniepoint.data.models.Citizen;
import dreamdev.moniepoint.data.models.ElectionType;
import dreamdev.moniepoint.data.models.RegisteredVoter;
import dreamdev.moniepoint.dtos.response.NominateCandidateResponse;

import java.util.List;

public class CandidateMapper {

    public static NominateCandidateResponse map(Candidate candidate,
                                                ElectionType election,
                                                List<String> otherCandidateNames) {
        RegisteredVoter voter = candidate.getRegisteredVoter();
        Citizen citizen = voter.getCitizen();

        NominateCandidateResponse response = new NominateCandidateResponse();
        response.setCandidateId(candidate.getId());
        response.setFirstName(citizen.getFirstName());
        response.setLastName(citizen.getLastName());
        response.setStateOfOrigin(citizen.getStateOfOrigin());
        response.setNationalID(citizen.getNationalID());
        response.setElectionName(election.getName());
        response.setElectionId(election.getId());
        response.setOtherCandidateNames(otherCandidateNames);
        response.setNominatedAt(candidate.getNominatedAt());
        return response;
    }

}
