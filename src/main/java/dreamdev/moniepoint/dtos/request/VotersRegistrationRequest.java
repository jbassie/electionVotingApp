package dreamdev.moniepoint.dtos.request;

import lombok.Data;

@Data
public class VotersRegistrationRequest {
    private String nationalID;
    private String dateOfBirth;
    private String phoneNumber;

}
