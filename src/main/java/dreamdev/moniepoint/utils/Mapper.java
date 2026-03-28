package dreamdev.moniepoint.utils;

import dreamdev.moniepoint.data.models.Citizen;
import dreamdev.moniepoint.dtos.request.CitizenRegistrationRequest;
import dreamdev.moniepoint.dtos.response.CitizenRegistrationResponse;

public class Mapper {
    public static Citizen map(CitizenRegistrationRequest citizenRegistrationRequest){
        Citizen citizen = new Citizen();
        citizen.setFirstName(citizenRegistrationRequest.getFirstName());
        citizen.setNationalID(NationalIDGenerator.generate());
        citizen.setLastName(citizenRegistrationRequest.getLastName());
        citizen.setDateOfBirth(citizenRegistrationRequest.getDateOfBirth());
        citizen.setStateOfOrigin(citizenRegistrationRequest.getStateOfOrigin());
        citizen.setGender(citizenRegistrationRequest.getGender());
        return citizen;
    }


    public static CitizenRegistrationResponse map(Citizen savedCitizen){
        CitizenRegistrationResponse citizenRegistrationResponse = new CitizenRegistrationResponse();
        citizenRegistrationResponse.setFirstName(savedCitizen.getFirstName());
        citizenRegistrationResponse.setLastName(savedCitizen.getLastName());
        citizenRegistrationResponse.setNationalID(savedCitizen.getNationalID());
        return citizenRegistrationResponse;
    }
}

//
//public static User map(UserRegistrationRequest userRegistrationRequest) {
//    User user = new User();
//    user.setFirstName(userRegistrationRequest.getFirstName());
//    user.setLastName(userRegistrationRequest.getLastName());
//    user.setEmail(userRegistrationRequest.getEmail());
//    user.setPassword(userRegistrationRequest.getPassword());
//    user.setRole("USER");
//    return user;
//}