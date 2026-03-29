package dreamdev.moniepoint.dtos.request;

import lombok.Data;

@Data
public class VotersLoginRequest {
    private String nationalID;
    private String password;
}
