package dreamdev.moniepoint.utils;

import dreamdev.moniepoint.data.models.Citizen;
import dreamdev.moniepoint.data.models.ElectionType;
import dreamdev.moniepoint.data.models.RegisteredVoter;
import dreamdev.moniepoint.data.models.Vote;
import dreamdev.moniepoint.dtos.response.CastVoteResponse;

public class VoteMapper {

    public static CastVoteResponse map(Vote vote, ElectionType election) {
        RegisteredVoter voter = vote.getCandidate().getRegisteredVoter();
        Citizen citizen = voter.getCitizen();

        CastVoteResponse response = new CastVoteResponse();
        response.setVoterID(vote.getVoterId());
        response.setCandidateName(citizen.getFirstName() + " " + citizen.getLastName());
        response.setElectionName(election.getName());
        response.setElectionTypeId(election.getId());
        response.setVotedAt(vote.getVotedAt());
        return response;
    }

}
