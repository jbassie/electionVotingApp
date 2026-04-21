package dreamdev.moniepoint.services;

import dreamdev.moniepoint.dtos.request.AdminLoginRequest;
import dreamdev.moniepoint.dtos.request.AdminRegistrationRequest;
import dreamdev.moniepoint.dtos.response.AdminLoginResponse;
import dreamdev.moniepoint.dtos.response.AdminRegistrationResponse;

public interface AdminService {
    AdminRegistrationResponse register(AdminRegistrationRequest request);
    AdminLoginResponse login(AdminLoginRequest request);
}
