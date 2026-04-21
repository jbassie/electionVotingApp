package dreamdev.moniepoint.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminLoginResponse {
    private String adminId;
    private String username;
    private boolean isLoggedIn;
}
