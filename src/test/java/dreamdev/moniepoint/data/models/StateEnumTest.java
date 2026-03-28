package dreamdev.moniepoint.data.models;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StateEnumTest {

    @Test
    public void testValidState_ReturnsCorrectResult() {
        String stateName = "akwa_Ibom";
        assertTrue(StateEnum.isValidState(stateName));
    }

    @Test
    public void testInvalidState_ReturnsFalse() {
        assertFalse(StateEnum.isValidState("WAKANDA"));
    }
}
