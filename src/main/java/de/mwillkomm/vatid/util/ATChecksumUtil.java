package de.mwillkomm.vatid.util;

import org.springframework.stereotype.Component;

@Component
public class ATChecksumUtil {

    private static final int[] at_weight = new int[]{ 1, 2, 1, 2, 1, 2, 1};

    public boolean verify(String input) {
        try {
            String digits = input.substring(3);
            int quer = 0;
            for (int i = 0; i < digits.length() - 1; ++i) {
                int cur_val = digits.charAt(i) - '0';
                quer += quersumme(cur_val * at_weight[i]);
            }
            int rest = 96 - quer;
            int last_digit = digits.charAt(digits.length() - 1) - '0';
            return rest % 10 == last_digit;
        } catch (Exception e) {
            return false;
        }
    }

    public int quersumme(int zahl) {
        int quer, rest;
        quer = 0;
        while (zahl > 0) {
            rest = zahl % 10;
            quer = quer + rest;
            zahl = zahl / 10;
        }
        return quer;
    }

}
