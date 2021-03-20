package de.mwillkomm.vatid.util;

import org.springframework.stereotype.Component;

@Component
public class FRChecksumUtil {

    /**
     * taken from
     * https://blog.dotnetframework.org/2020/08/26/convert-french-siret-siren-to-vat-tva-in-c/
     *
     * and
     * https://en.wikipedia.org/wiki/VAT_identification_number
     *
     * but does not work with VAT IDs like
     * FR K7 39985941 2
     * FR 4Z 12345678 2
     *
     * which contain Letters as checksum letters (which are allowed as well).
     * according to
     * https://formvalidation.io/guide/validators/vat/french-vat-number
     *
     *
     * The last 9 digits consist of 8 digits SIREN + 1 checksum.
     * The two letter between FR and the siren are the checksum checked here,
     * which can also be letters. This is very confusing.
     *
     * @param input
     * @return
     */
    public boolean verify(String input) {
        try {
            String digits = input.substring(4, 13);
            String check = input.substring(2,4);
            Long total = Long.parseLong(digits);
            total = (12 + 3* (total % 97)) % 97;
            return check.equals(total.toString());
        } catch (Exception e) {
            return false;
        }
    }

    public int getLastDigit(String digits) {
        return digits.charAt(digits.length() - 1) - '0';
    }

}
