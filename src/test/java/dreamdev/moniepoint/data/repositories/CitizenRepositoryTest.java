package dreamdev.moniepoint.data.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CitizenRepositoryTest {

    @Autowired
    private CitizensRepository citizensRepository;

    @Test
    public void newRepository_ReturnsZero() {
        assertEquals(0L, citizensRepository.count());
    }
}
