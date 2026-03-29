package dreamdev.moniepoint.dtos.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CastVoteResponse {

    private String voterID;
    private String candidateName;
    private String electionName;
    private String electionTypeId;
    private LocalDateTime votedAt;

}
