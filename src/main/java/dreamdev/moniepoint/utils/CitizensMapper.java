package dreamdev.moniepoint.utils;

import dreamdev.moniepoint.data.models.Citizen;
import dreamdev.moniepoint.data.models.StateEnum;
import dreamdev.moniepoint.dtos.request.CitizenRegistrationRequest;
import dreamdev.moniepoint.dtos.response.CitizenRegistrationResponse;
import dreamdev.moniepoint.exceptions.*;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CitizensMapper {

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


}

