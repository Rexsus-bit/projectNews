package com.stat.statserver.util;

import java.security.SecureRandom;
import java.util.UUID;

public class RandomDataGenerator {
    private static final SecureRandom rand = new SecureRandom();

    public static String getRandomString() {
        UUID randomUUID = UUID.randomUUID();
        return randomUUID.toString().replaceAll("-", "");
    }

    public static long getRandomLongNumber() {
        return Math.abs(rand.nextLong());
    }

}
