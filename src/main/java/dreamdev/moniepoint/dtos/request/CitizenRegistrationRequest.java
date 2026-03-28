package dreamdev.moniepoint.dtos.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CitizenRegistrationRequest {

        private String firstName;
        private String lastName;
        private LocalDate dateOfBirth;
        private String stateOfOrigin;
        private String gender;

}
