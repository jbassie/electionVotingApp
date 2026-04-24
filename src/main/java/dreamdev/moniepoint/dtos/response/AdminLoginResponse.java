package dreamdev.moniepoint.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminLoginResponse {
    @JsonProperty("admin_id")
    private String adminId;
    private String email;
    private boolean isLoggedIn;
}
