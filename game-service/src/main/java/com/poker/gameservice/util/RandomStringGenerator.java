package com.poker.gameservice.util;

import java.util.Random;

public class RandomStringGenerator {
    private static int CODE_LENGTH = 6;
    private static String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    public static String generate() {
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();

        while (salt.length() < CODE_LENGTH) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }

        return salt.toString();
    }
}
