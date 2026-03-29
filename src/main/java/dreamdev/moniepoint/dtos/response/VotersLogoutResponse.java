package dreamdev.moniepoint.dtos.response;

import lombok.Data;

@Data
public class VotersLogoutResponse {
    private String voterID;
    private boolean isLoggedIn;
}
