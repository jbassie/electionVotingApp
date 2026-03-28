package dreamdev.moniepoint.services;

import dreamdev.moniepoint.data.models.Citizen;
import dreamdev.moniepoint.data.repositories.CitizensRepository;
import dreamdev.moniepoint.dtos.request.CitizenRegistrationRequest;
import dreamdev.moniepoint.dtos.response.CitizenRegistrationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static dreamdev.moniepoint.utils.Mapper.*;

@Service
public class CitizenServiceImpl implements CitizenService {

    @Autowired
    private CitizensRepository citizensRepository;



    @Override
    public CitizenRegistrationResponse register(CitizenRegistrationRequest citizenRegistrationRequest){
        Citizen savedCitizen = citizensRepository.save(map(citizenRegistrationRequest));
        return map(savedCitizen);
    }

}
