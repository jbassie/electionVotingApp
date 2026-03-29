package dreamdev.moniepoint.services;

import dreamdev.moniepoint.dtos.request.VotersLoginRequest;
import dreamdev.moniepoint.dtos.request.VotersLoginRequest;
import dreamdev.moniepoint.dtos.request.VotersRegistrationRequest;
import dreamdev.moniepoint.dtos.response.VotersRegistrationResponse;

public interface VoterService {
    VotersRegistrationResponse register(VotersRegistrationRequest votersRegistrationRequest);
    VotersRegistrationResponse login(VotersLoginRequest votersLoginRequest);
    void logout(String nationalID);
}