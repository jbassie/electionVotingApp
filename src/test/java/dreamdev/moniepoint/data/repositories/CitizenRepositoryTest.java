package dreamdev.moniepoint.data.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CitizenRepositoryTest {

    @Autowired
    private CitizensRepository citizensRepository;

    @BeforeEach
    public void setUp(){
        citizensRepository.deleteAll();
    }

    @Test
    public void newRepository_ReturnsZero() {
        assertEquals(0L, citizensRepository.count());
    }
}
