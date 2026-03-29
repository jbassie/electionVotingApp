package dreamdev.moniepoint.services;

import dreamdev.moniepoint.dtos.request.NominateCandidateRequest;
import dreamdev.moniepoint.dtos.response.NominateCandidateResponse;

import java.util.List;

public interface CandidateService {
    NominateCandidateResponse nominate(NominateCandidateRequest request);
    List<NominateCandidateResponse> getCandidatesByElection(String electionTypeId);
}
