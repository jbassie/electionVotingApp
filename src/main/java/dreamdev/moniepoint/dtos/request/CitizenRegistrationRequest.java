package dreamdev.moniepoint.dtos.request;

import lombok.Data;



@Data
public class CitizenRegistrationRequest {

        private String firstName;
        private String lastName;
        private String phoneNumber;
        private String dateOfBirth;
        private String stateOfOrigin;
        private String gender;

}
