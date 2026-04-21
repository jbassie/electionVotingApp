package dreamdev.moniepoint.dtos.request;

import lombok.Data;

@Data
public class NominateCandidateRequest {
    private String nominatorVoterID;
    private String nomineeVoterID;
    private String electionId;
}
