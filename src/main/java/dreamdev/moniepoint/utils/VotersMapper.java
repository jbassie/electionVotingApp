package dreamdev.moniepoint.utils;

import dreamdev.moniepoint.data.models.Citizen;
import dreamdev.moniepoint.data.models.RegisteredVoter;
import dreamdev.moniepoint.dtos.request.VotersRegistrationRequest;
import dreamdev.moniepoint.dtos.response.VotersLoginResponse;
import dreamdev.moniepoint.dtos.response.VotersLogoutResponse;
import dreamdev.moniepoint.dtos.response.VotersRegistrationResponse;
import dreamdev.moniepoint.exceptions.CitizenNotOfAgeException;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class VotersMapper {
        public static void validateAge(Citizen citizen) {

            int age = LocalDate.now().getYear() - citizen.getDateOfBirth().getYear();
            if (age < 18) {
                throw new CitizenNotOfAgeException("Citizen must be at least 18 years old to register as a voter. " +
                        "Current age: " + age);
            }
        }

        public static RegisteredVoter map(VotersRegistrationRequest request, Citizen citizen) {
            validateAge(citizen);
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
            response.setVoterID(registeredVoter.getVoterID());
            response.setAge(String.valueOf(
                    LocalDate.now().getYear() - registeredVoter.getCitizen().getDateOfBirth().getYear()
            ));
            return response;
        }

    public static VotersLoginResponse mapToLoginResponse(RegisteredVoter registeredVoter) {
        VotersLoginResponse loginResponse = new VotersLoginResponse();
        loginResponse.setLoggedIn(registeredVoter.isLoggedIn());
        loginResponse.setVoterID(registeredVoter.getVoterID());
        return loginResponse;
    }

    public static VotersLogoutResponse mapToLogoutResponse(RegisteredVoter registeredVoter) {
        VotersLogoutResponse logoutResponse = new VotersLogoutResponse();
        logoutResponse.setLoggedIn(registeredVoter.isLoggedIn());
        logoutResponse.setVoterID(registeredVoter.getNationalID());
        return logoutResponse;
    }
}
