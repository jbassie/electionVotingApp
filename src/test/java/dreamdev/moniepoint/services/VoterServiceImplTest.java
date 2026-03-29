package dreamdev.moniepoint.services;

import dreamdev.moniepoint.data.repositories.CitizensRepository;
import dreamdev.moniepoint.data.repositories.RegisteredVotersRepository;
import dreamdev.moniepoint.dtos.request.CitizenRegistrationRequest;
import dreamdev.moniepoint.dtos.request.VotersRegistrationRequest;
import dreamdev.moniepoint.exceptions.AlreadyRegisteredException;
import dreamdev.moniepoint.exceptions.CitizenNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class VoterServiceImplTest {

        @Autowired
        private VoterService voterService;

        @Autowired
        private CitizenService citizenService;

        @Autowired
        private CitizensRepository citizensRepository;

        @Autowired
        private RegisteredVotersRepository registeredVotersRepository;

        private VotersRegistrationRequest votersRegistrationRequest;
        private CitizenRegistrationRequest citizenRegistrationRequest;

        @BeforeEach
        public void setUp() {
            registeredVotersRepository.deleteAll();
            citizensRepository.deleteAll();


            citizenRegistrationRequest = new CitizenRegistrationRequest();
            citizenRegistrationRequest.setFirstName("John");
            citizenRegistrationRequest.setLastName("Doe");
            citizenRegistrationRequest.setDateOfBirth("1990-05-21");
            citizenRegistrationRequest.setPhoneNumber("09423477350");
            citizenRegistrationRequest.setGender("Male");
            citizenRegistrationRequest.setStateOfOrigin("LAGOS");


            var citizenResponse = citizenService.register(citizenRegistrationRequest);


            votersRegistrationRequest = new VotersRegistrationRequest();
            votersRegistrationRequest.setNationalID(citizenResponse.getNationalID());
            votersRegistrationRequest.setPassword("password123");
        }

        @Test
        public void register_successTest() {
            assertEquals(0L, registeredVotersRepository.count());
            voterService.register(votersRegistrationRequest);
            assertEquals(1L, registeredVotersRepository.count());
        }

    @Test
    public void registerTwiceWithSameNationalID_throwsExceptionTest() {
        assertEquals(0L, registeredVotersRepository.count());
        voterService.register(votersRegistrationRequest);
        assertThrows(AlreadyRegisteredException.class,
                () -> voterService.register(votersRegistrationRequest));
        assertEquals(1L, registeredVotersRepository.count());
    }

    @Test
    public void registerWithInvalidNationalID_throwsExceptionTest() {
        votersRegistrationRequest.setNationalID("INVALID123");
        assertThrows(CitizenNotFoundException.class,
                () -> voterService.register(votersRegistrationRequest));
        assertEquals(0L, registeredVotersRepository.count());
    }
}
