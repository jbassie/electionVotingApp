package dreamdev.moniepoint.data.models;

import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;


@Data
@Document
public class Citizen {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String nationalID;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String stateOfOrigin;
    private String gender;

}

