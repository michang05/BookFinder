package com.dmi.test.bookfinder.util;

import java.util.Random;

/**
 * Created by Mikey on 7/3/2016.
 */
public class DataUtil {


    private static int MAX_LENGTH = 10;

    public static String randomStr() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(MAX_LENGTH);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }


    public static int randomInt(){

        Random r = new Random();
        int i1 = r.nextInt(80 - 65) + 65;

        return i1;
    }

}
