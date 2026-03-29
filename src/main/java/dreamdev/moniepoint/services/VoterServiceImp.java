package dreamdev.moniepoint.services;


import dreamdev.moniepoint.data.models.Citizen;
import dreamdev.moniepoint.data.models.RegisteredVoter;
import dreamdev.moniepoint.data.repositories.CitizensRepository;
import dreamdev.moniepoint.data.repositories.RegisteredVotersRepository;
import dreamdev.moniepoint.dtos.request.VotersLoginRequest;
import dreamdev.moniepoint.dtos.request.VotersRegistrationRequest;

import dreamdev.moniepoint.dtos.response.VotersLoginResponse;
import dreamdev.moniepoint.dtos.response.VotersLogoutResponse;
import dreamdev.moniepoint.dtos.response.VotersRegistrationResponse;
import dreamdev.moniepoint.exceptions.AlreadyRegisteredException;
import dreamdev.moniepoint.exceptions.CitizenNotFoundException;
import dreamdev.moniepoint.exceptions.InvalidLoginDetailsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static dreamdev.moniepoint.utils.VotersMapper.*;

@Service
public class VoterServiceImp implements VoterService {
    @Autowired
    private CitizensRepository citizensRepository;

    @Autowired
    private  RegisteredVotersRepository registeredVotersRepository;


    @Override
   public VotersRegistrationResponse register(VotersRegistrationRequest votersRegistrationRequest){
        Citizen citizen = citizensRepository.findByNationalID(
                        votersRegistrationRequest.getNationalID())
                .orElseThrow(() -> new CitizenNotFoundException(
                        "Citizen not found with ID: " + votersRegistrationRequest.getNationalID()));


        if(registeredVotersRepository.findByNationalID(
                votersRegistrationRequest.getNationalID()).isPresent())
            throw new AlreadyRegisteredException("Citizen is already a registered voter");
        RegisteredVoter savedRegisteredVoter = registeredVotersRepository.save(map(votersRegistrationRequest, citizen));

        return map(savedRegisteredVoter);
    }

    @Override
    public VotersLoginResponse login(VotersLoginRequest loginRequest) {
        RegisteredVoter voter = registeredVotersRepository
                .findByVoterID(loginRequest.getVoterID())
                .orElseThrow(() -> new InvalidLoginDetailsException(
                        "No registered voter found with ID: " + loginRequest.getVoterID()));

        if (!voter.getPassword().equals(loginRequest.getPassword()))
            throw new InvalidLoginDetailsException("Incorrect password");

        voter.setLoggedIn(true);
        registeredVotersRepository.save(voter);

        return mapToLoginResponse(voter);
    }

    @Override
    public VotersLogoutResponse logout(String voterID) {
        Optional<RegisteredVoter> optionalVoter = registeredVotersRepository.findByVoterID(voterID);
        if (optionalVoter.isEmpty() ) throw new InvalidLoginDetailsException("No registered voter found with ID: " + voterID);

        RegisteredVoter voter = optionalVoter.get();

        voter.setLoggedIn(false);
        RegisteredVoter registeredVoter = registeredVotersRepository.save(voter);

        return mapToLogoutResponse(registeredVoter);
    }

}

