package dreamdev.moniepoint.services;

import dreamdev.moniepoint.data.repositories.CitizensRepository;
import dreamdev.moniepoint.dtos.request.CitizenRegistrationRequest;
import dreamdev.moniepoint.utils.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CitizenServiceImplTest {

    @Autowired
    private CitizenService citizenService;
    private CitizenRegistrationRequest registrationRequest;

    @Autowired
    private CitizensRepository citizensRepository;

    @BeforeEach
    public void setUp(){
        citizensRepository.deleteAll();
        registrationRequest = new CitizenRegistrationRequest();

        registrationRequest.setFirstName("John");
        registrationRequest.setLastName("Doe");
        registrationRequest.setDateOfBirth("2014-03-17");
        registrationRequest.setGender("Male");
        registrationRequest.setStateOfOrigin("Lagos");

    }



    @Test
    public void register_successTest(){
        assertEquals(0L, citizensRepository.count());
        citizenService.register(registrationRequest);
        assertEquals(1L, citizensRepository.count());
    }

}
