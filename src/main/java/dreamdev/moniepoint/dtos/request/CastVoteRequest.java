package dreamdev.moniepoint.dtos.request;

import lombok.Data;

@Data
public class CastVoteRequest {
    private String voterID;
    private String electionTypeId;
    private String candidateId;
}
