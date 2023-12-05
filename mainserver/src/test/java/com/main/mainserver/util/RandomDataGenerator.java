package com.main.mainserver.util;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class RandomDataGenerator {
    private static final SecureRandom rand = new SecureRandom();

    public static LocalDateTime getRandomLocalDateTime() {
        return LocalDateTime.now().plusSeconds(getRandomNumberBetweenValues(0, 999999999))
                .truncatedTo(ChronoUnit.SECONDS);
    }

    public static String getRandomString() {
        UUID randomUUID = UUID.randomUUID();
        return randomUUID.toString().replaceAll("-", "");
    }

    public static String getRandomEmail() {
        UUID randomUUID = UUID.randomUUID();
        return randomUUID.toString().replaceAll("-", "") + "@mail.com";
    }

    public static long getRandomNumberBetweenValues(int min, int max) {
        return (long) (Math.random() * (max - min + 1) + min);
    }

    public static long getRandomLongNumber() {
        return Math.abs(rand.nextLong());
    }

}
