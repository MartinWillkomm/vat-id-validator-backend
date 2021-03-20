package de.mwillkomm.vatid.validator.vatid;

import de.mwillkomm.vatid.util.FRChecksumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class FRVatValidator implements IVatIdValidator {

    private static final String[] SUPPORTED_COUNTRY_CODES = new String[]{ "FR" };
    // Pattern: FR - two letters (except O and I) - 9 numbers
    private static final Pattern PATTERN = Pattern.compile("^FR[a-hj-np-zA-HJ-NP-Z0-9]{2}\\d{9}$", Pattern.CASE_INSENSITIVE);
    private final FRChecksumUtil frChecksumUtil;

    @Autowired
    public FRVatValidator(FRChecksumUtil frChecksumUtil) {
        this.frChecksumUtil = frChecksumUtil;
    }

    @Override
    public String[] getSupportedCountryCodes() {
        return SUPPORTED_COUNTRY_CODES;
    }

    @Override
    public boolean isValidVatID(String input) {
        if (PATTERN.matcher(input).matches()) {
            return frChecksumUtil.verify(input);
        }
        return false;
    }
}
