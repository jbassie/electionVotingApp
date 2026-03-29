package dreamdev.moniepoint.dtos.response;

import dreamdev.moniepoint.data.models.ElectionStatus;
import lombok.Data;

import java.util.List;

@Data
public class ElectionResultsResponse {

    private String electionTypeId;
    private String electionName;
    private ElectionStatus status;
    private List<CandidateResultItem> candidates;

    @Data
    public static class CandidateResultItem {
        private String candidateId;
        private String firstName;
        private String lastName;
        private String stateOfOrigin;
        private int voteCount;
    }

}
