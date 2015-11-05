package com.realdolmen.rdfleet.utils;

/**
 * Created by OCPAX79 on 5/11/2015.
 */
public class LicensePlateGenerator {
    private static String generateLetters(int amount) {
        String letters = "";
        int n = 'Z' - 'A' + 1;
        for (int i = 0; i < amount; i++) {
            char c = (char) ('A' + Math.random() * n);
            letters += c;
        }
        return letters;
    }

    private static String generateDigits(int amount) {
        String digits = "";
        int n = '9' - '0' + 1;
        for (int i = 0; i < amount; i++) {
            char c = (char) ('0' + Math.random() * n);
            digits += c;
        }
        return digits;
    }

    public static String generateLicensePlate() {
        return String.format("%s-%s-%s", generateDigits(1), generateLetters(3), generateDigits(3));
    }
}
