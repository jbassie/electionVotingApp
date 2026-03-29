package dreamdev.moniepoint.data.models;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@Document
public class RegisteredVoter {
    @Id
    private String id;

    @DocumentReference
    private Citizen citizen;

    private String voterID;
    private LocalDate registeredAt;
    private String password;
    private boolean isLoggedIn;
    private Map<String, Boolean> hasVoted = new HashMap<>();



}


