package dreamdev.moniepoint.data.repositories;

import dreamdev.moniepoint.data.models.Citizen;
import dreamdev.moniepoint.data.models.RegisteredVoter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RegisteredVoterRepositoryTest {

    @Autowired
    private CitizensRepository citizensRepository;

    @Autowired
    private RegisteredVotersRepository registeredVotersRepository;

    @AfterEach
    void tearDown(){
        registeredVotersRepository.deleteAll();
    }

    @Test
    public void newRepository_ReturnsZero() {
        assertEquals(0L, registeredVotersRepository.count());
    }

    @Test
    void testSaveRegisteredVoter() {
        RegisteredVoter registeredVoter = new RegisteredVoter();
        citizen.setFirstName("John");
        citizen.setLastName("Doe");
        citizen.setNationalID("NGN1234567890");
        citizen.setDateOfBirth(LocalDate.of(1990, 1, 1));
        citizen.setStateOfOrigin("LAGOS");
        citizen.setGender("Male");

        Citizen saved = citizensRepository.save(citizen);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getNationalID()).isEqualTo("NGN1234567890");
        assertThat(saved.getFirstName()).isEqualTo("John");
    }


}
