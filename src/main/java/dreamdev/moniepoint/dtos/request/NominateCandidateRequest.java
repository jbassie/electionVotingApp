package dreamdev.moniepoint.dtos.request;

import lombok.Data;

@Data
public class NominateCandidateRequest {
    private String voterID;
    private String electionTypeId;
}
