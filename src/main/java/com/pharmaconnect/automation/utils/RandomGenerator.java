package com.pharmaconnect.automation.utils;

import java.util.Random;

public class RandomGenerator {

    public static String generateRandomEmail(String name) {
        Random random = new Random();

        // Clean the name: lowercase, remove non-alpha characters
        String cleanName = name.toLowerCase().replaceAll("[^a-z]", "");

        // Generate a random number suffix (2–4 digits)
        int suffix = 10 + random.nextInt(9990);

        return cleanName + suffix + "@pc.com";
    }
    public static String generateRandomPhone() {
        Random random = new Random();
        StringBuilder phone = new StringBuilder();

        // First digit cannot be 0
        phone.append(1 + random.nextInt(9));

        // Remaining 9 digits
        for (int i = 0; i < 9; i++) {
            phone.append(random.nextInt(10));
        }

        return phone.toString();
    }
}
