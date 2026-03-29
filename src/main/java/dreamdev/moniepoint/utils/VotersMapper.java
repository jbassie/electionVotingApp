package dreamdev.moniepoint.utils;

import dreamdev.moniepoint.data.models.Citizen;
import dreamdev.moniepoint.data.models.RegisteredVoter;
import dreamdev.moniepoint.dtos.request.VotersRegistrationRequest;
import dreamdev.moniepoint.dtos.response.VotersRegistrationResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class VotersMapper {


        public static RegisteredVoter map(VotersRegistrationRequest request, Citizen citizen) {
            RegisteredVoter registeredVoter = new RegisteredVoter();
            registeredVoter.setCitizen(citizen);
            registeredVoter.setNationalID(citizen.getNationalID());
            registeredVoter.setVoterID(VoterIDGenerator.generate());
            registeredVoter.setPassword(request.getPassword());
            registeredVoter.setRegisteredAt(LocalDateTime.now());
            registeredVoter.setLoggedIn(false);
            return registeredVoter;
        }

        public static VotersRegistrationResponse map(RegisteredVoter registeredVoter) {
            VotersRegistrationResponse response = new VotersRegistrationResponse();
            response.setFirstName(registeredVoter.getCitizen().getFirstName());
            response.setLastName(registeredVoter.getCitizen().getLastName());
            response.setNationalID(registeredVoter.getCitizen().getNationalID());
            response.setPhoneNumber(registeredVoter.getCitizen().getPhoneNumber());
            response.setStateOfOrigin(registeredVoter.getCitizen().getStateOfOrigin());
            response.setVotersID(registeredVoter.getVoterID());
            response.setAge(String.valueOf(
                    LocalDate.now().getYear() - registeredVoter.getCitizen().getDateOfBirth().getYear()
            ));
            return response;
        }
    }
}
