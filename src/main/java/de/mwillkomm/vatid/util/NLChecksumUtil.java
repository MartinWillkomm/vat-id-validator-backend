package de.mwillkomm.vatid.util;

import org.springframework.stereotype.Component;

@Component
public class NLChecksumUtil {

    private static final int[] nl_weight = new int[]{ 9, 8, 7, 6, 5, 4, 3, 2};

    public boolean verify(String input) {
        try {
            String digits = input.substring(2, 11);
            int sum = 0;
            for (int i = 0; i < digits.length() - 1; ++i) {
                int cur_val = number(digits, i);
                sum += cur_val * nl_weight[i];
            }
            int rest = sum % 11;
            if (rest == 10) {
                rest = 0;
            }
            return rest == getLastDigit(digits);
        } catch (Exception e) {
            return false;
        }

    }

    public int getLastDigit(String digits) {
        return digits.charAt(digits.length() - 1) - '0';
    }

    public int number(String digits, int i) {
        return digits.charAt(i) - '0';
    }
}
