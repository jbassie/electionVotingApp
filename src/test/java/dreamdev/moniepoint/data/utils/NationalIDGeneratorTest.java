package dreamdev.moniepoint.data.utils;

import dreamdev.moniepoint.utils.NationalIDGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NationalIDGeneratorTest {
    @Test

    public void generateID_StartsWithNIG(){
        String id = NationalIDGenerator.generate();
        assertTrue(id.startsWith("NGN"));
    }

    @Test
    public void generatedID_HasCorrectLength(){
        String id = NationalIDGenerator.generate();
        assertEquals(13, id.length());
    }

    @Test
    public void twoGeneratedIDs_AreUnique() {
        String id1 = NationalIDGenerator.generate();
        String id2 = NationalIDGenerator.generate();
        assertNotEquals(id1, id2);
    }
}
