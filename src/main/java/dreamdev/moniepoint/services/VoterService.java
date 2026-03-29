package dreamdev.moniepoint.services;

import dreamdev.moniepoint.dtos.request.VotersLoginRequest;
import dreamdev.moniepoint.dtos.request.VotersLoginRequest;
import dreamdev.moniepoint.dtos.request.VotersRegistrationRequest;
import dreamdev.moniepoint.dtos.response.VotersLoginResponse;
import dreamdev.moniepoint.dtos.response.VotersLogoutResponse;
import dreamdev.moniepoint.dtos.response.VotersRegistrationResponse;

public interface VoterService {
    VotersRegistrationResponse register(VotersRegistrationRequest votersRegistrationRequest);
    VotersLoginResponse login(VotersLoginRequest votersLoginRequest);
    VotersLogoutResponse logout(String voterID);
}