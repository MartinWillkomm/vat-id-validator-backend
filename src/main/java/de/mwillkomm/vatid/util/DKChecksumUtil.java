package de.mwillkomm.vatid.util;

import org.springframework.stereotype.Component;

@Component
public class DKChecksumUtil extends CommonChecksumUtil {

    private static final int[] dk_weight = new int[]{ 2, 7, 6, 5, 4, 3, 2, 1};

    public boolean verify(String input) {
        try {
            String digits = input.substring(2);
            int sum = 0;
            for (int i = 0; i < digits.length(); ++i) {
                int cur_val = getNumberAt(digits, i);
                sum += cur_val * dk_weight[i];
            }
            int rest = sum % 11;
            return rest == 0;
        } catch (Exception e) {
            return false;
        }
    }
}
