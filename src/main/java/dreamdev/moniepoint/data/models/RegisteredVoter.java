package dreamdev.moniepoint.data.models;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class RegisteredVoter {
    @Id
    private id;

}

id, citizen (ref → Citizen), voterId (auto-generated), password,
isLoggedIn (default: false), hasVoted: Map<electionId, Boolean> (default: empty
