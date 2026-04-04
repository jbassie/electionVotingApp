package dreamdev.moniepoint.data.repositories;


import dreamdev.moniepoint.data.models.Citizen;
import dreamdev.moniepoint.data.models.RegisteredVoter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RegisteredVoterRepositoryTest {

    @Autowired
    private RegisteredVotersRepository registeredVotersRepository;

    @Autowired
    private CitizensRepository citizensRepository;

    @AfterEach
    void tearDown(){
        registeredVotersRepository.deleteAll();
        citizensRepository.deleteAll();
    }

    @Test
    public void newRepository_ReturnsZero() {
        assertEquals(0L, registeredVotersRepository.count());
    }


    @Test
    void testSaveRegisteredVoter() {
        Citizen citizen = new Citizen();
        citizen.setFirstName("John");
        citizen.setLastName("Doe");
        citizen.setNationalID("NIG4829301746");
        citizen.setStateOfOrigin("LAGOS");
        citizen.setGender("Male");
        citizen.setDateOfBirth(LocalDate.of(1990, 5, 21));
        Citizen savedCitizen = citizensRepository.save(citizen);

        RegisteredVoter registeredVoter = new RegisteredVoter();
        registeredVoter.setCitizen(savedCitizen);
        registeredVoter.setVoterID("B012345235");
        registeredVoter.setPassword("1234");
        registeredVoter.setRegisteredAt(LocalDateTime.now());

        RegisteredVoter saved = registeredVotersRepository.save(registeredVoter);

        assertThat(saved.getId()).isNotNull();


    }


}
