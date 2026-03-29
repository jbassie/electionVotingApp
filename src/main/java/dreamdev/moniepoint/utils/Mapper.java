package dreamdev.moniepoint.utils;

import dreamdev.moniepoint.data.models.Citizen;
import dreamdev.moniepoint.data.models.RegisteredVoter;
import dreamdev.moniepoint.data.models.StateEnum;
import dreamdev.moniepoint.dtos.request.CitizenRegistrationRequest;
import dreamdev.moniepoint.dtos.request.VotersRegistrationRequest;
import dreamdev.moniepoint.dtos.response.CitizenRegistrationResponse;
import dreamdev.moniepoint.dtos.response.VotersRegistrationResponse;
import dreamdev.moniepoint.exceptions.*;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mapper {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDate parseDate(String dateOfBirth){
        try {
            return LocalDate.parse(dateOfBirth, DATE_FORMATTER);
        } catch (DateTimeException e){
            throw new IncorrectDatePattern("Invalid date format" + dateOfBirth + "Expected format is yyyy-MM-dd");
        }
    }

    public static void validate(CitizenRegistrationRequest citizenRegistrationRequest){
        if(!StateEnum.isValidState(citizenRegistrationRequest.getStateOfOrigin())) {
            throw new InvalidStateException("Invalid State" + citizenRegistrationRequest.getStateOfOrigin());
        }
    }

    public static void validatePhoneNumber(CitizenRegistrationRequest citizenRegistrationRequest) {
        String phoneNumber = citizenRegistrationRequest.getPhoneNumber();

        if (phoneNumber == null || phoneNumber.length() != 11 || !phoneNumber.matches("\\d{11}")) {
            throw new InvalidPhoneNumberException("Invalid phone number: " + phoneNumber +
                    ". Must be exactly 11 digits e.g. 08012345678");
        }
    }


    public static Citizen map(CitizenRegistrationRequest citizenRegistrationRequest){
        validate(citizenRegistrationRequest);
        Citizen citizen = new Citizen();
        citizen.setFirstName(citizenRegistrationRequest.getFirstName());
        citizen.setNationalID(NationalIDGenerator.generate());
        citizen.setLastName(citizenRegistrationRequest.getLastName());
        citizen.setPhoneNumber(citizenRegistrationRequest.getPhoneNumber());
        citizen.setDateOfBirth(parseDate(citizenRegistrationRequest.getDateOfBirth()));
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


    public static RegisteredVoter map(VotersRegistrationRequest votersRegistrationRequest, Citizen citizen){


        RegisteredVoter registeredVoter = new RegisteredVoter();
        registeredVoter.setCitizen(citizen);
        registeredVoter.setVoterID(VoterIDGenerator.generate());
        registeredVoter.setNationalID(citizen.getNationalID());
        registeredVoter.setPassword(votersRegistrationRequest.getPassword());
        registeredVoter.setRegisteredAt(LocalDateTime.now());
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

