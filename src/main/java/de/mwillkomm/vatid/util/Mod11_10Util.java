package de.mwillkomm.vatid.util;

import org.springframework.stereotype.Component;

/**
 * taken from
 * https://raw.githubusercontent.com/Moerin/testBDD/master/src/com/modp/checkdigits/CheckISO7064Mod11_10.java
 */
@Component
public class Mod11_10Util {

    public boolean verify(String input) {
        try {
            String digits = input.substring(2);
            int t = 10;
            for (int i = 0; i < digits.length() - 1; ++i) {
                t = (2 * f(t + number(digits, i))) % 11;
            }
            return (((t + getLastDigit(digits)) % 10) == 1);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * private helper function to simplify computations.
     *
     * @param x int
     * @return int
     */
    private int f(int x) {
        int val = x % 10;
        return (val == 0) ? 10 : val;
    }

    public int getLastDigit(String digits) {
        return digits.charAt(digits.length() - 1) - '0';
    }

    public int number(String digits, int i) {
        return digits.charAt(i) - '0';
    }

}
