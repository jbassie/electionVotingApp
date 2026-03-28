package dreamdev.moniepoint.dtos.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CitizensRegistrationRequest {

        private String firstName;
        private String lastName;
        private String nationalId;
        private String password;
        private LocalDate dateOfBirth;
        private String stateOfOrigin;
        private String gender;


}
