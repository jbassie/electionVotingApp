package dreamdev.moniepoint.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminRegistrationResponse {
    private String adminId;
    private String username;
}
