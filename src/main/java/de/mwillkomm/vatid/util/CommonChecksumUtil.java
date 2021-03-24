package de.mwillkomm.vatid.util;

public class CommonChecksumUtil {

    protected int getLastDigit(String digits) {
        return digits.charAt(digits.length() - 1) - '0';
    }

    protected int getNumberAt(String digits, int i) {
        return digits.charAt(i) - '0';
    }

    protected int getChecksum(int number) {
        int quer, rest;
        quer = 0;
        while (number > 0) {
            rest = number % 10;
            quer = quer + rest;
            number = number / 10;
        }
        return quer;
    }

}
