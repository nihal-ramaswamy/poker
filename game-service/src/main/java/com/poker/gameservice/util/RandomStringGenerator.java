package com.poker.gameservice.util;

import com.poker.gameservice.config.Constants;

import java.util.Random;

public class RandomStringGenerator {

    public static String generate() {

        int codeLen = Constants.CODE_LENGTH;

        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();

        while (salt.length() < codeLen) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }

        return salt.toString();
    }
}
