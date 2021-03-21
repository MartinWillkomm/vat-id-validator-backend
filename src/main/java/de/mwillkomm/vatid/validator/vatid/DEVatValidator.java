package de.mwillkomm.vatid.validator.vatid;

import de.mwillkomm.vatid.util.DEChecksumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class DEVatValidator implements IVatIdValidator {

    private static final String[] SUPPORTED_COUNTRY_CODES = new String[]{ "DE" };
    private static final Pattern PATTERN = Pattern.compile("^DE\\d{9}$", Pattern.CASE_INSENSITIVE);
    private final DEChecksumUtil deChecksumUtil;

    @Autowired
    public DEVatValidator(DEChecksumUtil deChecksumUtil) {
        this.deChecksumUtil = deChecksumUtil;
    }

    @Override
    public String[] getSupportedCountryCodes() {
        return SUPPORTED_COUNTRY_CODES;
    }

    @Override
    public boolean isValidVatID(String input) {
        if (PATTERN.matcher(input).matches()) {
            return deChecksumUtil.verify(input);
        } else {
            return false;
        }
    }
}
