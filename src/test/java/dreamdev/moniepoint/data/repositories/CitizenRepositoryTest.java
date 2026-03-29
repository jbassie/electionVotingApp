package dreamdev.moniepoint.data.repositories;

import dreamdev.moniepoint.data.models.Citizen;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CitizenRepositoryTest {

    @Autowired
    private CitizensRepository citizensRepository;

    @AfterEach
    void tearDown() {
        citizensRepository.deleteAll();
    }

    @Test
    public void newRepository_ReturnsZero() {
        assertEquals(0L, citizensRepository.count());
    }

    @Test
    void testSaveCitizen() {
        Citizen citizen = new Citizen();
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

    @Test
    void testFindByNationalID() {
        Citizen citizen = new Citizen();
        citizen.setFirstName("Jane");
        citizen.setLastName("Doe");
        citizen.setNationalID("NGN9876543210");
        citizen.setPhoneNumber("12345678901");
        citizen.setDateOfBirth(LocalDate.of(1995, 5, 20));
        citizen.setStateOfOrigin("KANO");
        citizen.setGender("Female");
        citizensRepository.save(citizen);

        Optional<Citizen> found = citizensRepository.findByNationalID("NGN9876543210");

        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("Jane");
    }

    @Test
    void testFindByNationalID_returnsEmpty_whenNotFound() {
        Optional<Citizen> found = citizensRepository.findByNationalID("NGN0000000000");
        assertThat(found).isEmpty();
    }

    @Test
    void testDateOfBirthPersistsCorrectly() {
        Citizen citizen = new Citizen();
        citizen.setFirstName("Ada");
        citizen.setLastName("Obi");
        citizen.setPhoneNumber("12345678901");
        citizen.setNationalID("NGN1111111111");
        citizen.setDateOfBirth(LocalDate.of(2000, 6, 15));
        citizen.setStateOfOrigin("ENUGU");
        citizen.setGender("Female");
        citizensRepository.save(citizen);

        Optional<Citizen> found = citizensRepository.findByNationalID("NGN1111111111");

        assertThat(found).isPresent();
        assertThat(found.get().getDateOfBirth()).isEqualTo(LocalDate.of(2000, 6, 15));
    }
}


