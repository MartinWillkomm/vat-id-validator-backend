package de.mwillkomm.vatid.util;

import org.springframework.stereotype.Component;

@Component
public class ATChecksumUtil extends CommonChecksumUtil {

    private static final int[] at_weight = new int[]{ 1, 2, 1, 2, 1, 2, 1};

    public boolean verify(String input) {
        try {
            String digits = input.substring(3);
            int quer = 0;
            for (int i = 0; i < digits.length() - 1; ++i) {
                int cur_val = digits.charAt(i) - '0';
                quer += getChecksum(cur_val * at_weight[i]);
            }
            int rest = 96 - quer;
            int last_digit = getLastDigit(digits);
            return rest % 10 == last_digit;
        } catch (Exception e) {
            return false;
        }
    }

}
