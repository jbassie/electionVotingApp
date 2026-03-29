package dreamdev.moniepoint.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;

@Data
@Document
public class Vote {

    @Id
    private String id;

    private String voterId;

    @DocumentReference
    private Candidate candidate;

    private String candidateId;

    private String electionTypeId;

    private LocalDateTime votedAt;

}
