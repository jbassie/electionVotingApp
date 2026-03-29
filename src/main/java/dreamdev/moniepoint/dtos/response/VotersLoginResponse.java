package dreamdev.moniepoint.dtos.response;

import lombok.Data;

@Data
public class VotersLoginResponse {
    private String voterID;
    private boolean isLoggedIn;
}
