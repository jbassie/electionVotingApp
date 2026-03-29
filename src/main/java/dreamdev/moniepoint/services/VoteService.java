package dreamdev.moniepoint.services;

import dreamdev.moniepoint.dtos.request.CastVoteRequest;
import dreamdev.moniepoint.dtos.response.CastVoteResponse;

public interface VoteService {
    CastVoteResponse castVote(CastVoteRequest request);
}
