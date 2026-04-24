package dreamdev.moniepoint.dtos.request;

import lombok.Data;

@Data
public class AdminRegistrationRequest {
    private String email;
    private String password;
}
