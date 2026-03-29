package dreamdev.moniepoint.dtos.request;

import lombok.Data;

@Data
public class VotersLoginRequest {
    private String voterID;
    private String password;
}
