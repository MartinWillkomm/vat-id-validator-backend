package de.mwillkomm.vatid.util;

import org.springframework.stereotype.Component;

/**
 * taken from
 *
 * https://www.braemoor.co.uk/software/downloads/jsvat.zip
 * https://www.kmstewart.com/resources/check-vat-number
 * https://en.wikipedia.org/wiki/VAT_identification_number
 *
 * there were no other resources available, especially for validating a 12-digit
 * branch traders variant, which could be faulty.
 *
 */
@Component
public class GBChecksumUtil extends CommonChecksumUtil {

    private static final int[] gb_weight = new int[]{ 8, 7, 6, 5, 4, 3, 2};

    public boolean verifyGovernmentDepartment(String input) {
        int numberPart = Integer.parseInt(input.substring(4, 7));
        return numberPart < 500;
    }

    public boolean verifyHealthAuthorities(String input) {
        int numberPart = Integer.parseInt(input.substring(4, 7));
        return numberPart >= 500 && numberPart < 1000;
    }

    public boolean verifyStandard(String input) {
        try {
            String digits = input.substring(2, 9);
            int digitsInt = Integer.parseInt(digits);
            int checkDigits = Integer.parseInt(input.substring(9, 11));
            // 0 VAT numbers disallowed!
            if (digitsInt == 0) {
                return false;
            }

            int total = 0;
            for (int i = 0; i < 7; ++i) { // don't run digits.length, it may be 12 digits long (branch traders group in the end)
                int cur_val = getNumberAt(digits, i);
                total += cur_val * gb_weight[i];
            }
            int cd = total;
            while (cd > 0) {
                cd = cd - 97;
            }
            cd = Math.abs(cd);
            if (cd == checkDigits) {
                if (digitsInt < 9990001 && (digitsInt < 100000 || digitsInt > 999999) && (digitsInt < 9490001 || digitsInt > 9700000)) {
                    return true;
                }
            }
            // new vat numbers from 2010 onwards;
            if (cd >= 55) {
                cd = cd - 55;
            }
            else {
                cd = cd + 42;
            }
            return cd == checkDigits && digitsInt > 1000000;
        } catch (Exception e) {
            return false;
        }

    }
}
