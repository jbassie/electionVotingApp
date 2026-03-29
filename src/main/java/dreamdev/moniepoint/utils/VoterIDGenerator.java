package dreamdev.moniepoint.utils;

import java.util.Random;

public class VoterIDGenerator {

    private static final Random random = new Random();

    public static String generate() {
        StringBuilder voterID = new StringBuilder();

        for(int i=0; i<2; i++){
            char letter = (char) ('A' + random.nextInt(26));
            voterID.append(letter);
        }

        for (int i = 0; i<6; i++){
            voterID.append(random.nextInt(10));
        }

        return voterID.toString();
    }
}
