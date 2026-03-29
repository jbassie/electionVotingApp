package dreamdev.moniepoint.dtos.response;

import lombok.Data;

@Data
public class VotersRegistrationResponse {

    private String firstName;
    private String lastName;
    private String age;
    private String nationalID;
    private String phoneNumber;
    private String votersID;
    private String stateOfOrigin;

}
