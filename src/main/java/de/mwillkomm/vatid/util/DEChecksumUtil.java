package de.mwillkomm.vatid.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * taken from
 * http://www.pruefziffernberechnung.de/U/USt-IdNr.shtml#PZDE
 * https://raw.githubusercontent.com/Moerin/testBDD/master/src/com/modp/checkdigits/CheckISO7064Mod11_10.java
 * https://www.braemoor.co.uk/software/downloads/jsvat.zip
 */
@Component
public class DEChecksumUtil extends CommonChecksumUtil {

    private static Logger logger = LoggerFactory.getLogger(DEChecksumUtil.class);

    /**
     * this is my own implementation, but it produces the same
     * result as the one taken from github (down below) and breamoor's in JS.
     *
     * which is somehow wrong, because braemoor's test data claims to
     * be validated correctly, but it fails in my case with some german vat ids.
     *
     * @param input
     * @return checksums validation status as boolean
     */
    public boolean verifyNew(String input) {
        try {
            String digits = input.substring(2);
            int checkDigit = getLastDigit(digits);
            int sum = 0;
            int prod = 10;
            for (int i = 0; i < digits.length() - 1; ++i) {
                sum = (prod + getNumberAt(digits, i)) % 10;
                if (sum == 0) {
                    sum = 10;
                }
                prod = (2 * sum) % 11;
                logger.debug("digit: {}, sum: {}, prod: {} ", getNumberAt(digits, i), sum, prod );
            }
            int expectedCheckDigit = diff(prod);
            return expectedCheckDigit == checkDigit;
        } catch (Exception e) {
            return false;
        }
    }

    public int diff(int prod) {
        int diff = (11 - prod);
        if (diff > 9) {
            diff = 0;
        }
        return diff;
    }

    public boolean verify(String input) {
        try {
            String digits = input.substring(2);
            int t = 10;
            for (int i = 0; i < digits.length() - 1; ++i) {
                t = (2 * f(t + getNumberAt(digits, i))) % 11;
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
}
