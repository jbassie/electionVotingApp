package dreamdev.moniepoint.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;

@Data
@Document
public class Candidate {

    @Id
    private String id;

    @DocumentReference
    private RegisteredVoter registeredVoter;

    private String electionTypeId;
    private int voteCount = 0;
    private LocalDateTime nominatedAt;
}