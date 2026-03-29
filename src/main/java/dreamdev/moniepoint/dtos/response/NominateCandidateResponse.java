package dreamdev.moniepoint.dtos.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class NominateCandidateResponse {

    private String candidateId;
    private String firstName;
    private String lastName;
    private String stateOfOrigin;
    private String nationalID;


    private String electionName;
    private String electionTypeId;


    private List<String> otherCandidateNames;

    private LocalDateTime nominatedAt;

}
