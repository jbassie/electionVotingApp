package dreamdev.moniepoint.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class VotersIDGeneratorTest {

    @Test
    public void generatedID_HasCorrectLength() {
        String id = VoterIDGenerator.generate();
        assertEquals(8, id.length());
    }

    @Test
    public void twoGeneratedIDs_AreUnique() {
        String id1 = VoterIDGenerator.generate();
        String id2 = VoterIDGenerator.generate();
        assertNotEquals(id1, id2);
    }
}
