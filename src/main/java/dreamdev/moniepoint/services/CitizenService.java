package dreamdev.moniepoint.services;

import dreamdev.moniepoint.dtos.request.CitizenRegistrationRequest;
import dreamdev.moniepoint.dtos.response.CitizenRegistrationResponse;

public interface CitizenService {

    CitizenRegistrationResponse register(CitizenRegistrationRequest citizenRegistrationRequest);
}
