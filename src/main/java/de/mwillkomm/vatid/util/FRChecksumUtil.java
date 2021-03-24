package de.mwillkomm.vatid.util;

import org.springframework.stereotype.Component;

@Component
public class FRChecksumUtil extends CommonChecksumUtil{

    /**
     * taken from
     * https://blog.dotnetframework.org/2020/08/26/convert-french-siret-siren-to-vat-tva-in-c/
     * https://en.wikipedia.org/wiki/VAT_identification_number
     *
     * this code does not work with vat ids like
     * FR K7 39985941 2 (SIREN 399859412 is valid, but has different checksum: https://www.societe.com/societe/altea-expertise-comptable-399859412.html -> FR06399859412
     * FR 4Z 12345678 2 (invalid, acc. eu commission)
     *
     * which contain Letters as checksum letters (which are allowed).
     * according to
     *
     * https://formvalidation.io/guide/validators/vat/french-vat-number
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
            long check = Long.parseLong(input.substring(2,4));
            String digits = input.substring(4, 13);
            long total = Long.parseLong(digits);
            total = (12 + 3* (total % 97)) % 97;
            return total == check;
        } catch (Exception e) {
            return false;
        }
    }
}
