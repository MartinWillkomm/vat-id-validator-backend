package de.mwillkomm.vatid.validator.vatid;

import de.mwillkomm.vatid.util.Mod11_10Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class DEVatValidator implements IVatIdValidator {

    private static final String[] SUPPORTED_COUNTRY_CODES = new String[]{ "DE" };
    private static final Pattern PATTERN = Pattern.compile("^DE\\d{9}$", Pattern.CASE_INSENSITIVE);
    private final Mod11_10Util mod11_10Util;

    @Autowired
    public DEVatValidator(Mod11_10Util mod11_10Util) {
        this.mod11_10Util = mod11_10Util;
    }

    @Override
    public String[] getSupportedCountryCodes() {
        return SUPPORTED_COUNTRY_CODES;
    }

    @Override
    public boolean isValidVatID(String input) {
        if (PATTERN.matcher(input).matches()) {
            return mod11_10Util.verify(input);
        } else {
            return false;
        }
    }
}
