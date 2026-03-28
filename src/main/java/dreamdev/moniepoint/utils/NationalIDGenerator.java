package dreamdev.moniepoint.utils;

import java.util.Random;

public class NationalIDGenerator {

    public static String generate() {
        Random random = new Random();
        StringBuilder id  = new StringBuilder("NGN");
        for (int i=0; i < 10; i++ ){
            id.append(random.nextInt(10));

        }
        return id.toString();
    }
}
